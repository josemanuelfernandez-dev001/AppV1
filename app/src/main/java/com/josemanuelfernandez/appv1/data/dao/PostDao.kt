package com.josemanuelfernandez.appv1.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.josemanuelfernandez.appv1.data.entity.Post

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: Post): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<Post>)

    @Update
    suspend fun update(post: Post)

    @Delete
    suspend fun delete(post: Post)

    @Query("SELECT * FROM posts WHERE id = :postId")
    suspend fun getPostById(postId: Long): Post?

    @Query("SELECT * FROM posts WHERE id = :postId")
    fun getPostByIdLive(postId: Long): LiveData<Post?>

    @Query("SELECT * FROM posts ORDER BY (upvotes - downvotes) DESC, timestamp DESC")
    fun getAllPostsByScore(): LiveData<List<Post>>

    @Query("SELECT * FROM posts ORDER BY timestamp DESC")
    fun getAllPostsByTime(): LiveData<List<Post>>

    @Query("UPDATE posts SET upvotes = upvotes + 1 WHERE id = :postId")
    suspend fun incrementUpvotes(postId: Long)

    @Query("UPDATE posts SET upvotes = upvotes - 1 WHERE id = :postId")
    suspend fun decrementUpvotes(postId: Long)

    @Query("UPDATE posts SET downvotes = downvotes + 1 WHERE id = :postId")
    suspend fun incrementDownvotes(postId: Long)

    @Query("UPDATE posts SET downvotes = downvotes - 1 WHERE id = :postId")
    suspend fun decrementDownvotes(postId: Long)
}
