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

## File system

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

## Database Schema
