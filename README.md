# Just Note

[![Build Status](https://travis-ci.org/tankery/just-note.svg?branch=master)](https://travis-ci.org/tankery/just-note)
[![Work In Progress](https://img.shields.io/badge/In%20Progress-5%25-yellow.svg?style=flat)](https://github.com/tankery/just-note/projects/1)

An open source notebook app to try new tech, in Android.

Also I'm trying to use best practise solution for any problems encounter.
So if you have better idea to how to solve specific problems,
feel free to fire an [issue](https://github.com/tankery/just-note/issues) to talk about it,
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

Feature list:

- [ ] Add, Delete, Edit text notes (Navigation, LiveData, Test, Files)
- [ ] Text styles: bold, italic, underlines, etc. (Markdown)
- [ ] Organize notes: tags, pin, archive (DB)
- [ ] Share notes
- [ ] Dynamic layout with content (RecyclerView, Coroutines)
- [ ] Add Image (Image caching, CameraX, Album)

Something may or may not add:

- Sync notes with Server (OkHttp, Retrofit)
- Sync notes with Google Drive, Dropbox, etc.
- Compress file system.
- Reminder, Todo list (Notification, WorkManager)
- Search keyword in notes
- Hand drawings
- Record audio in note

## Road map and planning

This project is still Working In Progress,
and I use [GitHub Project Boards](https://help.github.com/en/articles/about-project-boards) to manage the progress.

You can checkout [Projects](https://github.com/tankery/just-note/projects) tab to learn the working status of this project.

## Software Design Documents

I believe that things will going better when we have plans.
Software Design is the plan for how can we solve specific problem.

So you may want to checkout these documents before reading the code, to see how I
get things done from a high level perspective.

Document List:

- [Architecture Design](doc/architecture.md)
