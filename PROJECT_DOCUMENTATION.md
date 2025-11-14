# AppV1 - Social Comments Android Application

## Overview
This is a complete Android application implementing Facebook-style and Reddit-style comment systems with full CRUD operations, nested comments, voting, and real-time updates.

## Features

### Level 1: Room Database Implementation ✅
- **Room Database** with proper entity relationships
- **User Entity**: Stores user information (id, username, displayName, avatarUrl)
- **Comment Entity**: Stores comments with support for nesting (parentCommentId)
- **Post Entity**: Stores Reddit-style posts with voting
- **Vote Entity**: Tracks user votes on posts and comments
- **DAOs**: Complete CRUD operations for all entities
- **Seed Data**: Pre-populated with 8 users, 6 Facebook comments, 5 Reddit posts with comments

### Level 2: Facebook-Style Comments ✅
- Clean, Material Design 3 UI
- Post new comments
- Display user avatars (emoji-based)
- Show username and timestamp with relative time ("hace 2h")
- Like/unlike functionality
- Reply to comments (up to 3 levels of nesting)
- Collapse/expand reply threads
- Live updates using LiveData
- RecyclerView with efficient ViewHolder pattern

### Level 3: Reddit-Style Posts & Comments ✅
- Post creation with title and content
- Upvote/downvote system for posts
- Vote counter showing net score
- Nested comment threads (up to 5 levels)
- Collapse/expand functionality for threads
- Sort posts by score or time
- Independent voting for comments
- Visual indentation for nested comments

## Architecture

### MVVM Pattern
- **Model**: Room entities and database
- **View**: Fragments and RecyclerView Adapters
- **ViewModel**: FacebookViewModel and RedditViewModel

### Components
```
app/
├── data/
│   ├── dao/             # Data Access Objects
│   ├── database/        # Room Database configuration
│   ├── entity/          # Database entities
│   └── repository/      # Repository pattern implementation
├── ui/
│   ├── facebook/        # Facebook-style UI
│   │   └── adapter/     # RecyclerView adapter
│   └── reddit/          # Reddit-style UI
│       └── adapter/     # RecyclerView adapters
└── viewmodel/           # ViewModels for business logic
```

## Technical Stack

- **Language**: Kotlin
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: Room 2.6.1
- **UI**: Material Design 3
- **Async**: Kotlin Coroutines
- **Navigation**: Navigation Component 2.7.6
- **Lifecycle**: AndroidX Lifecycle components

## Key Dependencies

```kotlin
// Core
implementation("androidx.core:core-ktx:1.12.0")
implementation("androidx.appcompat:appcompat:1.6.1")
implementation("com.google.android.material:material:1.11.0")

// Navigation
implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
implementation("androidx.navigation:navigation-ui-ktx:2.7.6")

// Room Database
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
ksp("androidx.room:room-compiler:2.6.1")

// Lifecycle
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
```

## Features in Detail

### Facebook-Style Comments
- **Top-level comments**: Displayed in a RecyclerView sorted by timestamp
- **Nested replies**: Each comment can have multiple replies (up to 3 levels)
- **Like system**: Increment/decrement like counter
- **User selection**: Dropdown to select which user is commenting
- **Expand/Collapse**: "Ver X respuestas" / "Ocultar respuestas" buttons
- **Timestamp**: Relative time display (ahora, hace 2h, hace 3d, etc.)

### Reddit-Style Posts
- **Post creation**: Dialog with title, content, and user selection
- **Voting**: Upvote/downvote buttons with vote counter
- **Comments section**: Click post to view and add comments
- **Nested comments**: Up to 5 levels with visual indentation
- **Thread controls**: Expand/collapse nested comment threads
- **Sorting**: Posts sorted by score (upvotes - downvotes) by default

### Database Schema

#### Users Table
```sql
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL,
    displayName TEXT NOT NULL,
    avatarUrl TEXT
);
```

#### Comments Table
```sql
CREATE TABLE comments (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    userId INTEGER NOT NULL,
    postId INTEGER,
    parentCommentId INTEGER,
    content TEXT NOT NULL,
    timestamp INTEGER NOT NULL,
    likesCount INTEGER NOT NULL DEFAULT 0,
    type TEXT NOT NULL,  -- FACEBOOK or REDDIT
    FOREIGN KEY (userId) REFERENCES users(id),
    FOREIGN KEY (postId) REFERENCES posts(id),
    FOREIGN KEY (parentCommentId) REFERENCES comments(id)
);
```

#### Posts Table
```sql
CREATE TABLE posts (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    userId INTEGER NOT NULL,
    title TEXT NOT NULL,
    content TEXT NOT NULL,
    timestamp INTEGER NOT NULL,
    upvotes INTEGER NOT NULL DEFAULT 0,
    downvotes INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY (userId) REFERENCES users(id)
);
```

#### Votes Table
```sql
CREATE TABLE votes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    userId INTEGER NOT NULL,
    postId INTEGER,
    commentId INTEGER,
    isUpvote INTEGER NOT NULL,  -- 1 for upvote, 0 for downvote
    FOREIGN KEY (userId) REFERENCES users(id),
    FOREIGN KEY (postId) REFERENCES posts(id),
    FOREIGN KEY (commentId) REFERENCES comments(id)
);
```

## Building the Project

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK with API level 34
- Kotlin 1.9.20

### Build Steps
1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Build the project: `./gradlew assembleDebug`
5. Run on emulator or device

### Build Commands
```bash
# Clean build
./gradlew clean

# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Run tests
./gradlew test
```

## Project Structure Explained

### MainActivity
- Single activity with bottom navigation
- Hosts NavHostFragment for fragment navigation
- Material Toolbar at the top

### FacebookFragment
- Displays top-level comments in RecyclerView
- Input area at bottom with user selector and text input
- Comments load from Room database via ViewModel
- Live updates when new comments are added

### RedditFragment
- Displays posts in RecyclerView
- FAB button to create new post
- Click post to view comments in dialog
- Vote buttons for upvote/downvote

### Adapters
- **FacebookCommentAdapter**: Nested RecyclerView for replies
- **RedditPostAdapter**: Displays posts with voting UI
- **RedditCommentAdapter**: Nested comments with indentation

### ViewModels
- **FacebookViewModel**: Manages Facebook comment operations
- **RedditViewModel**: Manages Reddit posts and comments

### Repositories
- **FacebookRepository**: Abstracts data operations for Facebook features
- **RedditRepository**: Abstracts data operations for Reddit features

## Sample Data

### Users
- John Doe, Jane Smith, Mike Wilson, Sarah Jones, Alex Brown, Emily Davis, Chris Taylor, Lisa Martin

### Facebook Comments
- 6 top-level comments with various nested replies
- Likes ranging from 5 to 31
- Comments in Spanish

### Reddit Posts
- 5 posts about Android development topics
- Topics: Kotlin tutorial, Room vs SQLite, MVVM best practices, Material Design 3, RecyclerView optimization
- Each post has comments and replies
- Scores ranging from 81 to 258

## UI/UX Design

### Material Design 3
- Dynamic color scheme
- Elevated cards for comments and posts
- Text buttons for actions
- Proper spacing and typography
- Support for light/dark themes

### Navigation
- Bottom navigation with 2 tabs
- Fragment-based navigation using Navigation Component
- Smooth transitions between screens

### Responsive Design
- Supports different screen sizes
- ScrollView and RecyclerView for scrollable content
- Proper padding and margins

## Testing

The project structure supports:
- Unit tests for ViewModels and Repositories
- DAO tests for database operations
- UI tests with Espresso

## Future Enhancements

Potential improvements:
- [ ] User authentication
- [ ] Edit/delete comments and posts
- [ ] Image attachments
- [ ] Search functionality
- [ ] Push notifications
- [ ] User profiles
- [ ] Dark mode toggle in settings
- [ ] Pagination for large datasets
- [ ] Network sync with backend API

## License

This is a demo/educational project for learning Android development with Kotlin, Room, and MVVM architecture.

## Notes

This project was created to demonstrate:
1. Room Database setup and usage
2. MVVM architecture pattern
3. RecyclerView with nested adapters
4. Material Design 3 components
5. Navigation Component
6. LiveData and ViewModel
7. Kotlin Coroutines for async operations
8. Repository pattern for data abstraction

The app includes comprehensive seed data and is ready to run on any Android device with API 24+.
