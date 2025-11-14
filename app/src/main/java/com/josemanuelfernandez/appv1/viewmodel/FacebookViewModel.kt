package com.josemanuelfernandez.appv1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.josemanuelfernandez.appv1.data.database.AppDatabase
import com.josemanuelfernandez.appv1.data.entity.Comment
import com.josemanuelfernandez.appv1.data.entity.CommentType
import com.josemanuelfernandez.appv1.data.entity.User
import com.josemanuelfernandez.appv1.data.repository.FacebookRepository
import kotlinx.coroutines.launch

class FacebookViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FacebookRepository
    val topLevelComments: LiveData<List<Comment>>
    val allUsers: LiveData<List<User>>

    init {
        val database = AppDatabase.getDatabase(application)
        repository = FacebookRepository(database.userDao(), database.commentDao())
        topLevelComments = repository.getTopLevelComments()
        allUsers = repository.getAllUsers()
    }

    fun getReplies(parentCommentId: Long): LiveData<List<Comment>> {
        return repository.getReplies(parentCommentId)
    }

    fun addComment(content: String, userId: Long, parentCommentId: Long? = null) {
        viewModelScope.launch {
            val comment = Comment(
                userId = userId,
                content = content,
                parentCommentId = parentCommentId,
                type = CommentType.FACEBOOK
            )
            repository.addComment(comment)
        }
    }

    fun likeComment(commentId: Long) {
        viewModelScope.launch {
            repository.likeComment(commentId)
        }
    }

    fun unlikeComment(commentId: Long) {
        viewModelScope.launch {
            repository.unlikeComment(commentId)
        }
    }

    suspend fun getUserById(userId: Long): User? {
        return repository.getUserById(userId)
    }
}
