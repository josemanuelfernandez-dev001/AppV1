# AppV1 - Social Comments Android Application

A complete Android application featuring Facebook-style and Reddit-style comment systems with Room Database, MVVM architecture, and Material Design 3.

## Features

- **Facebook-Style Comments**: Post, like, and reply to comments with nested threading
- **Reddit-Style Posts**: Create posts, upvote/downvote, and participate in threaded discussions
- **Room Database**: Full offline support with SQLite via Room
- **Material Design 3**: Modern, beautiful UI with dynamic theming
- **MVVM Architecture**: Clean separation of concerns and testability

## Quick Start

```bash
# Open in Android Studio
# File > Open > Select this directory

# Or build from command line
./gradlew assembleDebug

# Install on device/emulator
./gradlew installDebug
```

## Documentation

See [PROJECT_DOCUMENTATION.md](PROJECT_DOCUMENTATION.md) for detailed information about:
- Architecture and components
- Database schema
- Features implementation
- Technical specifications
- Build instructions

## Requirements

- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK API 24-34
- Kotlin 1.9.20

## Tech Stack

- Kotlin
- Room Database
- MVVM Architecture
- Material Design 3
- Navigation Component
- Coroutines
- LiveData & ViewModel

## Sample Data

The app comes pre-loaded with:
- 8 sample users
- 6 Facebook-style comments with replies
- 5 Reddit-style posts with threaded comments

All in Spanish language for demonstration purposes.