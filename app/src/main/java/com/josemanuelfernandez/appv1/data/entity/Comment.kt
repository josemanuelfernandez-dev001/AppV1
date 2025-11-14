package com.josemanuelfernandez.appv1.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "comments",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Post::class,
            parentColumns = ["id"],
            childColumns = ["postId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId"), Index("postId"), Index("parentCommentId")]
)
data class Comment(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val postId: Long? = null, // For Reddit-style comments
    val parentCommentId: Long? = null, // For nested replies
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val likesCount: Int = 0,
    val type: CommentType = CommentType.FACEBOOK // FACEBOOK or REDDIT
)

enum class CommentType {
    FACEBOOK,
    REDDIT
}
