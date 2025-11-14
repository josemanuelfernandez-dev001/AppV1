package com.josemanuelfernandez.appv1.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.josemanuelfernandez.appv1.data.dao.CommentDao
import com.josemanuelfernandez.appv1.data.dao.PostDao
import com.josemanuelfernandez.appv1.data.dao.UserDao
import com.josemanuelfernandez.appv1.data.dao.VoteDao
import com.josemanuelfernandez.appv1.data.entity.Comment
import com.josemanuelfernandez.appv1.data.entity.CommentType
import com.josemanuelfernandez.appv1.data.entity.Post
import com.josemanuelfernandez.appv1.data.entity.User
import com.josemanuelfernandez.appv1.data.entity.Vote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [User::class, Comment::class, Post::class, Vote::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun commentDao(): CommentDao
    abstract fun postDao(): PostDao
    abstract fun voteDao(): VoteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    populateDatabase(database)
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun populateDatabase(database: AppDatabase) {
            val userDao = database.userDao()
            val commentDao = database.commentDao()
            val postDao = database.postDao()

            // Create sample users
            val users = listOf(
                User(username = "john_doe", displayName = "John Doe", avatarUrl = "👨"),
                User(username = "jane_smith", displayName = "Jane Smith", avatarUrl = "👩"),
                User(username = "mike_wilson", displayName = "Mike Wilson", avatarUrl = "👨‍💼"),
                User(username = "sarah_jones", displayName = "Sarah Jones", avatarUrl = "👩‍💻"),
                User(username = "alex_brown", displayName = "Alex Brown", avatarUrl = "🧑"),
                User(username = "emily_davis", displayName = "Emily Davis", avatarUrl = "👩‍🎨"),
                User(username = "chris_taylor", displayName = "Chris Taylor", avatarUrl = "👨‍🔬"),
                User(username = "lisa_martin", displayName = "Lisa Martin", avatarUrl = "👩‍🏫")
            )
            
            userDao.insertAll(users)

            // Create Facebook-style comments
            val fbComment1 = Comment(
                userId = 1,
                content = "¡Esta aplicación es increíble! Me encanta la interfaz limpia.",
                type = CommentType.FACEBOOK,
                likesCount = 15,
                timestamp = System.currentTimeMillis() - 7200000
            )
            val fbCommentId1 = commentDao.insert(fbComment1)

            val fbComment2 = Comment(
                userId = 2,
                content = "Totalmente de acuerdo. El diseño es muy intuitivo.",
                type = CommentType.FACEBOOK,
                parentCommentId = fbCommentId1,
                likesCount = 8,
                timestamp = System.currentTimeMillis() - 3600000
            )
            commentDao.insert(fbComment2)

            val fbComment3 = Comment(
                userId = 3,
                content = "¿Alguien sabe si tiene modo oscuro?",
                type = CommentType.FACEBOOK,
                likesCount = 23,
                timestamp = System.currentTimeMillis() - 5400000
            )
            val fbCommentId3 = commentDao.insert(fbComment3)

            val fbComment4 = Comment(
                userId = 4,
                content = "Sí, en configuración puedes activarlo 👍",
                type = CommentType.FACEBOOK,
                parentCommentId = fbCommentId3,
                likesCount = 12,
                timestamp = System.currentTimeMillis() - 1800000
            )
            commentDao.insert(fbComment4)

            val fbComment5 = Comment(
                userId = 5,
                content = "Excelente trabajo del equipo de desarrollo!",
                type = CommentType.FACEBOOK,
                likesCount = 31,
                timestamp = System.currentTimeMillis() - 9000000
            )
            commentDao.insert(fbComment5)

            val fbComment6 = Comment(
                userId = 6,
                content = "Me gustaría ver más opciones de personalización.",
                type = CommentType.FACEBOOK,
                likesCount = 5,
                timestamp = System.currentTimeMillis() - 10800000
            )
            commentDao.insert(fbComment6)

            // Create Reddit-style posts
            val post1 = Post(
                userId = 1,
                title = "Tutorial: Cómo empezar con Kotlin para Android",
                content = "Aquí comparto mi experiencia aprendiendo Kotlin. Es un lenguaje moderno y conciso que hace el desarrollo Android mucho más agradable. Los principales beneficios incluyen null safety, coroutines para programación asíncrona, y mucha menos boilerplate code.",
                upvotes = 142,
                downvotes = 8,
                timestamp = System.currentTimeMillis() - 86400000
            )
            val postId1 = postDao.insert(post1)

            val post2 = Post(
                userId = 3,
                title = "Comparación: Room vs SQLite puro",
                content = "Después de usar ambos en producción, aquí está mi análisis. Room reduce significativamente el código boilerplate, proporciona validación en tiempo de compilación, y se integra perfectamente con LiveData y Coroutines. La única desventaja es el tiempo de build ligeramente mayor.",
                upvotes = 89,
                downvotes = 3,
                timestamp = System.currentTimeMillis() - 172800000
            )
            val postId2 = postDao.insert(post2)

            val post3 = Post(
                userId = 5,
                title = "Las mejores prácticas de arquitectura MVVM",
                content = "MVVM es el patrón recomendado por Google para apps Android. Separación de concerns, testabilidad mejorada, y manejo reactivo de UI son las principales ventajas. Importante: ViewModel no debe tener referencias a Views o Context.",
                upvotes = 215,
                downvotes = 12,
                timestamp = System.currentTimeMillis() - 259200000
            )
            val postId3 = postDao.insert(post3)

            val post4 = Post(
                userId = 7,
                title = "Material Design 3: Guía completa",
                content = "La nueva versión de Material Design trae esquemas de color dinámicos, componentes actualizados y mejor accesibilidad. El sistema de tokens de diseño hace que mantener consistencia visual sea más fácil que nunca.",
                upvotes = 178,
                downvotes = 5,
                timestamp = System.currentTimeMillis() - 345600000
            )
            val postId4 = postDao.insert(post4)

            val post5 = Post(
                userId = 2,
                title = "Optimización de RecyclerView: Consejos prácticos",
                content = "RecyclerView es crucial para apps con listas. Usa DiffUtil para actualizaciones eficientes, implementa ViewHolder correctamente, y considera paginar datos grandes. También importante: usar setHasFixedSize(true) cuando sea apropiado.",
                upvotes = 267,
                downvotes = 9,
                timestamp = System.currentTimeMillis() - 432000000
            )
            val postId5 = postDao.insert(post5)

            // Create Reddit-style comments for posts
            val redditComment1 = Comment(
                userId = 2,
                postId = postId1,
                content = "Excelente tutorial! Me ayudó mucho cuando estaba empezando.",
                type = CommentType.REDDIT,
                likesCount = 45,
                timestamp = System.currentTimeMillis() - 82800000
            )
            val redditCommentId1 = commentDao.insert(redditComment1)

            val redditComment2 = Comment(
                userId = 4,
                postId = postId1,
                parentCommentId = redditCommentId1,
                content = "A mí también. La parte de coroutines fue especialmente útil.",
                type = CommentType.REDDIT,
                likesCount = 23,
                timestamp = System.currentTimeMillis() - 79200000
            )
            commentDao.insert(redditComment2)

            val redditComment3 = Comment(
                userId = 6,
                postId = postId1,
                content = "¿Tienen planes de hacer uno sobre Flow?",
                type = CommentType.REDDIT,
                likesCount = 18,
                timestamp = System.currentTimeMillis() - 75600000
            )
            commentDao.insert(redditComment3)

            val redditComment4 = Comment(
                userId = 3,
                postId = postId2,
                content = "Totalmente de acuerdo. Room hace el desarrollo mucho más rápido.",
                type = CommentType.REDDIT,
                likesCount = 31,
                timestamp = System.currentTimeMillis() - 169200000
            )
            commentDao.insert(redditComment4)

            val redditComment5 = Comment(
                userId = 8,
                postId = postId3,
                content = "MVVM cambió completamente cómo estructuro mis apps. Gran artículo!",
                type = CommentType.REDDIT,
                likesCount = 67,
                timestamp = System.currentTimeMillis() - 255600000
            )
            val redditCommentId5 = commentDao.insert(redditComment5)

            val redditComment6 = Comment(
                userId = 1,
                postId = postId3,
                parentCommentId = redditCommentId5,
                content = "Sí, la testabilidad mejora muchísimo con MVVM.",
                type = CommentType.REDDIT,
                likesCount = 34,
                timestamp = System.currentTimeMillis() - 252000000
            )
            commentDao.insert(redditComment6)

            val redditComment7 = Comment(
                userId = 4,
                postId = postId5,
                content = "DiffUtil es un game changer. Antes mis listas eran muy lentas.",
                type = CommentType.REDDIT,
                likesCount = 89,
                timestamp = System.currentTimeMillis() - 428400000
            )
            commentDao.insert(redditComment7)
        }
    }
}
