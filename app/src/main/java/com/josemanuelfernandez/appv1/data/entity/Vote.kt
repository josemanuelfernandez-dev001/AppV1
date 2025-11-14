package com.josemanuelfernandez.appv1.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "votes",
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
        ),
        ForeignKey(
            entity = Comment::class,
            parentColumns = ["id"],
            childColumns = ["commentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId"), Index("postId"), Index("commentId")]
)
data class Vote(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val postId: Long? = null,
    val commentId: Long? = null,
    val isUpvote: Boolean // true for upvote, false for downvote
)
