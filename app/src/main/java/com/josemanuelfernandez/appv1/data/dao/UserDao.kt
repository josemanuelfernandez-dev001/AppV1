package com.josemanuelfernandez.appv1.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.josemanuelfernandez.appv1.data.entity.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<User>)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Long): User?

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserByIdLive(userId: Long): LiveData<User?>

    @Query("SELECT * FROM users")
    fun getAllUsers(): LiveData<List<User>>

    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): User?
}
