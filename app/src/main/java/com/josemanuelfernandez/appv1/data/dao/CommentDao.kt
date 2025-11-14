package com.josemanuelfernandez.appv1.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.josemanuelfernandez.appv1.data.entity.Comment
import com.josemanuelfernandez.appv1.data.entity.CommentType

@Dao
interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(comment: Comment): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(comments: List<Comment>)

    @Update
    suspend fun update(comment: Comment)

    @Delete
    suspend fun delete(comment: Comment)

    @Query("SELECT * FROM comments WHERE id = :commentId")
    suspend fun getCommentById(commentId: Long): Comment?

    @Query("SELECT * FROM comments WHERE type = :type AND parentCommentId IS NULL ORDER BY timestamp DESC")
    fun getTopLevelComments(type: CommentType): LiveData<List<Comment>>

    @Query("SELECT * FROM comments WHERE parentCommentId = :parentId ORDER BY timestamp ASC")
    fun getReplies(parentId: Long): LiveData<List<Comment>>

    @Query("SELECT * FROM comments WHERE postId = :postId AND parentCommentId IS NULL ORDER BY likesCount DESC, timestamp DESC")
    fun getPostComments(postId: Long): LiveData<List<Comment>>

    @Query("UPDATE comments SET likesCount = likesCount + 1 WHERE id = :commentId")
    suspend fun incrementLikes(commentId: Long)

    @Query("UPDATE comments SET likesCount = likesCount - 1 WHERE id = :commentId")
    suspend fun decrementLikes(commentId: Long)
}
