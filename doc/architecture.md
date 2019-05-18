# Architecture of Just Note

Just Note is based on MVVM architecture, with support from Android's [Jetpack components](https://developer.android.com/jetpack/).

Things are assembled by following parts:

- **Model**: Notes are stored in Android's file system, using simplified Markdown syntax
(at first is just pure text, then I'll adding feature like text styles, images, etc.)
All notes are organized in Database, using [Room](https://developer.android.com/topic/libraries/architecture/room) library.
Checkout [File System](#file-system) and [Database Schema](#database-schema) for details. 
- **ViewModel**: using [LiveData](https://developer.android.com/topic/libraries/architecture/livedata),
A lifecycle-aware way to observe data changes, and using
[ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) library
to convert then to what View needs.
- **View**: View layer is the surface to interact with, using [Material Design](https://material.io/) for UI widgets,
using [Data Binding](https://developer.android.com/topic/libraries/data-binding) to reduce `findViewById`s,
using [Paging](https://developer.android.com/topic/libraries/architecture/paging/) to dynamic load notes from underlayer,
and using [Navigation](https://developer.android.com/guide/navigation/) to handle page routing.

## Data Structure

I believe that the data structure is a cornerstone of good software, so let's first talk about the data structure of Just Note. 

### File system

Each note is stored as an folder, with a index.md to record the text node, and other medias like images in same folder.
Each folder has a unique name, created from UUID.
Each media file has a generated name, I use first 6 character of SHA1 of file content,
if conflicts find, expand 2 character, and so on.
The database file is place in the root with all of the notes folder.

So the file system should be something like this:

```
|
+- notes.db
|
+- e0b0f196-5f10-4fc1-a083-031fdca5bc86/
|  +- index.md
|  +- 13cccf.png
|  `- 430ce3.jpg
|
+- 754a6b9b-5df9-4b4a-8a64-c76be13a74d5/
|  `- index.md
|
`- 21cad61e-8e2a-4afe-bbb9-7eac8e7d3999/
   `- index.md
```

Also I may going to compress each folder so we can occupy less space.
The compress/decompress can be managed with an LRU cache,
that mare sure we only have a few activated notes.

### Database Schema

Database is used to listing some metadata and organize notes.

To organize notes, user can pin, achieve, delete and adding tags for notes.

In order to implement, a straight forward way is put all this into note table,
but you can imaging that when we need to add some new features, like adding a "Shared with me" flag, in future,
we must migrate our notes table, and modify whole bunch of note rows.

So I think a graceful way to solve this, is treat all these flag as a special `tag`,
so as we developed the "tag" feature, pins, achieves, etc. can take advantage of "tag" system.

So we'll have there tables for our notes and tags: `note`, `tag` and `list`,
list is a table to link notes and tags together, actually, as you can see,
this is a "Many to Many" relationship.

So tables are defined as below:

```kotlin
@Entity(tableName = "note")
data class Note(
    @PrimaryKey val id: String,
    val createTimestamp: Long,
    val updateTimestamp: Long,
    val title: String?,
    val abstract: String?
)

@Entity(tableName = "tag")
data class Tag(
    @PrimaryKey val id: String,
    val createTimestamp: Long,
    val name: String
)

@Entity(tableName = "list",
    primaryKeys = ["noteId", "tagId"],
    foreignKeys = [
        ForeignKey(entity = Note::class,
            parentColumns = ["id"],
            childColumns = ["noteId"]),
        ForeignKey(entity = Tag::class,
            parentColumns = ["id"],
            childColumns = ["tagId"])
    ]
)
data class List(
    val noteId: String,
    val tagId: String
)
```

### Server API

Although I don't have a plan to develop a server currently,
but we can first think about how to design the Server API,
so once we decide to make a server, the process will be much easier and faster.

Also, thinking about API can help us survey the data structure,
make it flexible and robust.

So let's begin.

Overall, the note sync problem, is a simplified bi-direction syncing problem,
which may summarized to following 3 steps:

1. Download latest data from server.
2. Compare and merge data with server and local.
3. Upload local changes to server.

If we want to save bandwidth, first step can be split in two:

1. Download latest **metadata** from server.
2. Only download modified or missing data from server.

So let's try to define some APIs:

**Get metadata**

```
GET /notes?offset=20&limit=10
```

or get from single note:

```
GET /notes/{noteId}/metadata
```

**Get note files**

```
GET /notes/{noteId}/{filename}
```

First we can get `index.md`, then, load media files if need.

**Update or create note**

First, update or create metadata of a note

```
PUT /notes/{nodeId}
```

with metadata in request body

Second, upload note files:

```
PUT /notes/{nodeId}/{filename}
```

**Delete note forever**

```
DELETE /notes/{noteId}
```



