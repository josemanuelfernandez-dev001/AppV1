package com.josemanuelfernandez.appv1.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "posts",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId")]
)
data class Post(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val title: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val upvotes: Int = 0,
    val downvotes: Int = 0
) {
    val score: Int
        get() = upvotes - downvotes
}
