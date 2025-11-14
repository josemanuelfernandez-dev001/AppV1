package com.josemanuelfernandez.appv1.data.dao

import androidx.room.*
import com.josemanuelfernandez.appv1.data.entity.Vote

@Dao
interface VoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vote: Vote): Long

    @Update
    suspend fun update(vote: Vote)

    @Delete
    suspend fun delete(vote: Vote)

    @Query("SELECT * FROM votes WHERE userId = :userId AND postId = :postId")
    suspend fun getPostVote(userId: Long, postId: Long): Vote?

    @Query("SELECT * FROM votes WHERE userId = :userId AND commentId = :commentId")
    suspend fun getCommentVote(userId: Long, commentId: Long): Vote?

    @Query("DELETE FROM votes WHERE userId = :userId AND postId = :postId")
    suspend fun deletePostVote(userId: Long, postId: Long)

    @Query("DELETE FROM votes WHERE userId = :userId AND commentId = :commentId")
    suspend fun deleteCommentVote(userId: Long, commentId: Long)
}
