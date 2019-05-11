# Just Note

[![Build Status](https://travis-ci.org/tankery/note-app.svg?branch=master)](https://travis-ci.org/tankery/note-app)

An open source notebook app to try new tech, in Android.

Also I'm trying to use best practise solution for any problems encounter.
So if you have better idea to how to solve specific problems,
feel free to fire an [issue](https://github.com/tankery/note-app/issues) to talk about it,
thanks in advance.

## Tech Stack

- Project: Gradle, Travis CI
- Programming: Kotlin, KTX, Coroutines
- Libraries: Android Jetpack
- Architecture: MVVM architecture

The project is building using Gradle, it's of cause the best choice for any pure Android application. Other alternatives are ANT, BUCK, etc.

It also integrated with Travis CI, to check/verify every pull request, and release new version of App.

The programing language I choose is Kotlin, not Java any more, which is future of Android development.
With support of KTX, things may become easier to deal with Android APIs.

For concurrency and threading, I decide to give Coroutines a try.
Android's `Thread`, `AsyncTask` are heavy, and may need lots of boilerplate code.
ReactiveX is good, I've used is in other projects, but it's a different way to thinking,
and have a steep learning curve, so I won't use it in this project.
Coroutines is light weight, and have a very good design, definitely worth a shot.

In this project, I will first try to use a Jetpack component to solve problems,
for example, use ViewModel and LiveData to create a responsive data flow.
Or if there exists a really great library like OkHttp, Glide, etc., I will
introduce those library without hesitation.

## Features and Technologies

Feature list (Road map):

- [ ] Add, Delete, Edit text notes (Navigation, LiveData, Test, Files)
- [ ] Organize notes: tags, pin, archive (DB)
- [ ] Share notes
- [ ] Dynamic layout with content (RecyclerView, Coroutines)
- [ ] Add Image (Image caching, CameraX, Album)

Something may or may not add:

- Sync notes with Server (OkHttp, Retrofit)
- Sync notes with Google Drive, Dropbox, etc.
- Reminder, Todo list (Notification, WorkManager)
- Search keyword in notes
- Hand drawings
- Record audio in note
