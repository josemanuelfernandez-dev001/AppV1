# User Guide - AppV1 Social Comments

## Overview

AppV1 is a social commenting application that demonstrates two popular comment systems:
1. **Facebook-Style Comments** - Simple likes and nested replies
2. **Reddit-Style Posts & Comments** - Voting system with deeply threaded discussions

## Getting Started

When you launch the app, you'll see two tabs at the bottom:
- 📱 **Facebook Style** - For Facebook-like comments
- 📰 **Reddit Style** - For Reddit-like posts

## Facebook-Style Comments

### What You'll See

```
┌─────────────────────────────────────────┐
│  Social Comments App                    │
├─────────────────────────────────────────┤
│                                         │
│  👨 John Doe                            │
│  hace 2 horas                           │
│  ¡Esta aplicación es increíble!         │
│  Me encanta la interfaz limpia.         │
│  [Me gusta] 15 Me gusta [Responder]     │
│                                         │
│  👩 Jane Smith                          │
│  hace 1 hora                            │
│  Totalmente de acuerdo. El diseño es    │
│  muy intuitivo.                         │
│  [Me gusta] 8 Me gusta [Responder]      │
│                                         │
├─────────────────────────────────────────┤
│  [Seleccionar usuario ▼]                │
│  [Escribe un comentario...        ]     │
│  [Publicar]                             │
└─────────────────────────────────────────┘
```

### Features

#### 1. View Comments
- Scroll through existing comments
- See user avatars (emoji-based)
- View usernames and timestamps
- See like counts

#### 2. Like Comments
- Tap "Me gusta" to like a comment
- Counter updates immediately
- Tap again to unlike

#### 3. Post New Comments
1. Select your user from dropdown at bottom
2. Type your comment in the text field
3. Tap "Publicar" to post

#### 4. Reply to Comments
1. Tap "Responder" on any comment
2. Your reply will be nested under that comment
3. Replies are indented to show hierarchy

#### 5. View/Hide Replies
- If a comment has replies, you'll see "Ver X respuestas"
- Tap to expand and view replies
- Tap again to collapse ("Ocultar respuestas")

### Comment Hierarchy

Comments can be nested up to 3 levels deep:
```
Main Comment
└── Reply
    └── Reply to Reply
        └── Reply to Reply to Reply
```

## Reddit-Style Posts & Comments

### What You'll See

```
┌─────────────────────────────────────────┐
│  Social Comments App                    │
├─────────────────────────────────────────┤
│  ↑ 142  Tutorial: Cómo empezar con     │
│   ↓     Kotlin para Android             │
│         Por john_doe • hace 1 día       │
│         Aquí comparto mi experiencia... │
│         [5 comentarios]                 │
├─────────────────────────────────────────┤
│  ↑ 89   Comparación: Room vs SQLite    │
│   ↓     puro                            │
│         Por mike_wilson • hace 2 días   │
│         Después de usar ambos en...     │
│         [1 comentario]                  │
├─────────────────────────────────────────┤
│                                    [+]  │
└─────────────────────────────────────────┘
```

### Features

#### 1. View Posts
- Scroll through posts sorted by score (upvotes - downvotes)
- See post titles, authors, timestamps
- Preview content (first few lines)
- See vote counts and comment counts

#### 2. Vote on Posts
- Tap ↑ (upvote) to increase score
- Tap ↓ (downvote) to decrease score
- Tap again to remove your vote
- Score updates immediately

#### 3. Create New Post
1. Tap the [+] floating button
2. Dialog appears:
   ```
   Crear nuevo post
   ┌─────────────────────────┐
   │ [Seleccionar usuario ▼] │
   │ [Título del post]        │
   │ [Contenido (opcional)]   │
   │ [Cancelar] [Crear]       │
   └─────────────────────────┘
   ```
3. Select user, enter title and content
4. Tap "Crear" to publish

#### 4. View & Comment on Posts
1. Tap on any post to open comments dialog
2. You'll see:
   ```
   Tutorial: Cómo empezar con Kotlin
   ┌──────────────────────────────────┐
   │ ↑ john_doe • hace 2h             │
   │ 45                               │
   │ ↓ Excelente tutorial!            │
   │   [Responder] [Expandir]         │
   │                                  │
   │   | ↑ jane_smith • hace 1h       │
   │   | 23                           │
   │   | ↓ A mí también.              │
   │   |   [Responder]                │
   ├──────────────────────────────────┤
   │ [Escribe un comentario...  ]     │
   │ [Publicar Comentario]            │
   └──────────────────────────────────┘
   ```

#### 5. Comment on Posts
1. In the post comments dialog
2. Type your comment at the bottom
3. Tap "Publicar Comentario"

#### 6. Reply to Comments
1. Tap "Responder" on any comment
2. Dialog appears for your reply
3. Reply is nested under the original comment

#### 7. Vote on Comments
- Same as posts: tap ↑ or ↓
- Vote count updates immediately

#### 8. Expand/Collapse Threads
- If a comment has replies: tap "Expandir"
- Replies appear indented with a vertical line
- Tap "Colapsar" to hide

### Comment Hierarchy

Reddit-style comments can nest up to 5 levels deep:
```
Main Comment
│
├── Reply 1
│   │
│   ├── Reply 1.1
│   │   │
│   │   └── Reply 1.1.1
│   │       │
│   │       └── Reply 1.1.1.1
│   │
│   └── Reply 1.2
│
└── Reply 2
```

## Time Display

Timestamps are shown in relative format:
- **Ahora** - Just now
- **hace 5m** - 5 minutes ago
- **hace 2h** - 2 hours ago
- **hace 3d** - 3 days ago
- **15/11/2024** - Date if older than 7 days

## Pre-loaded Sample Data

The app comes with sample data to explore:

### Users (8)
- John Doe 👨
- Jane Smith 👩
- Mike Wilson 👨‍💼
- Sarah Jones 👩‍💻
- Alex Brown 🧑
- Emily Davis 👩‍🎨
- Chris Taylor 👨‍��
- Lisa Martin 👩‍🏫

### Facebook Comments (6+)
Various comments with replies about the app, all in Spanish

### Reddit Posts (5)
1. Kotlin tutorial (Score: 134, 3 comments)
2. Room vs SQLite comparison (Score: 86, 1 comment)
3. MVVM best practices (Score: 203, 2 comments)
4. Material Design 3 guide (Score: 173)
5. RecyclerView optimization (Score: 258, 1 comment)

## Tips & Tricks

### Facebook Style
- **Quick Like**: Tap "Me gusta" to like
- **Deep Threads**: Replies can go 3 levels deep
- **Collapse Clutter**: Hide reply threads when not needed
- **User Context**: Avatar color matches user

### Reddit Style
- **Vote Wisely**: Upvote quality content, downvote low quality
- **Deep Discussions**: Comments can nest 5 levels
- **Thread Navigation**: Use expand/collapse for long threads
- **Visual Cues**: Vertical lines show comment depth

## Understanding the UI

### Material Design 3
The app uses modern Material Design 3:
- **Cards**: Comments and posts in elevated cards
- **Colors**: Purple theme with proper contrast
- **Typography**: Clear, readable fonts
- **Spacing**: Consistent padding and margins
- **Buttons**: Text buttons for secondary actions
- **FAB**: Floating action button for main action

### Bottom Navigation
- **Tab 1** (left): Facebook-style comments
- **Tab 2** (right): Reddit-style posts
- Tap to switch between views
- Current tab is highlighted

### Dialogs
Used for:
- Creating new posts
- Viewing post comments
- Replying to comments
- Selecting users

## Data Persistence

All data is stored locally using Room Database:
- **Offline First**: Works without internet
- **Instant Updates**: Changes appear immediately
- **Persistent**: Data survives app restart
- **Efficient**: Indexed queries for fast access

## Architecture

The app follows modern Android best practices:
- **MVVM Pattern**: Clean separation of concerns
- **LiveData**: Reactive UI updates
- **Coroutines**: Smooth, non-blocking operations
- **Repository Pattern**: Abstract data sources
- **Room Database**: Type-safe database access

---

## Summary

AppV1 demonstrates two popular social commenting patterns:

**Facebook Style**: 
- Simple and friendly
- Quick likes
- Casual conversations
- 3-level threading

**Reddit Style**:
- Structured discussions
- Community voting
- Deep threading
- Quality ranking

Both systems use:
- Real-time updates
- Offline support
- Clean Material Design
- Intuitive interactions

Enjoy exploring both commenting styles! 🚀
