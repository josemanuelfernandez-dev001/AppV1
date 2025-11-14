package com.josemanuelfernandez.appv1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.josemanuelfernandez.appv1.data.database.AppDatabase
import com.josemanuelfernandez.appv1.data.entity.Comment
import com.josemanuelfernandez.appv1.data.entity.CommentType
import com.josemanuelfernandez.appv1.data.entity.Post
import com.josemanuelfernandez.appv1.data.entity.User
import com.josemanuelfernandez.appv1.data.repository.RedditRepository
import kotlinx.coroutines.launch

class RedditViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RedditRepository
    val allUsers: LiveData<List<User>>
    
    private val _sortByScore = MutableLiveData(true)
    val posts: LiveData<List<Post>>

    init {
        val database = AppDatabase.getDatabase(application)
        repository = RedditRepository(
            database.userDao(),
            database.postDao(),
            database.commentDao(),
            database.voteDao()
        )
        allUsers = repository.getAllUsers()
        posts = repository.getAllPostsByScore()
    }

    fun setSortByScore(byScore: Boolean) {
        _sortByScore.value = byScore
    }

    fun getPostComments(postId: Long): LiveData<List<Comment>> {
        return repository.getPostComments(postId)
    }

    fun getReplies(parentCommentId: Long): LiveData<List<Comment>> {
        return repository.getReplies(parentCommentId)
    }

    fun addPost(title: String, content: String, userId: Long) {
        viewModelScope.launch {
            val post = Post(
                userId = userId,
                title = title,
                content = content
            )
            repository.addPost(post)
        }
    }

    fun addComment(content: String, postId: Long, userId: Long, parentCommentId: Long? = null) {
        viewModelScope.launch {
            val comment = Comment(
                userId = userId,
                postId = postId,
                content = content,
                parentCommentId = parentCommentId,
                type = CommentType.REDDIT
            )
            repository.addComment(comment)
        }
    }

    fun upvotePost(postId: Long, userId: Long) {
        viewModelScope.launch {
            repository.upvotePost(postId, userId)
        }
    }

    fun downvotePost(postId: Long, userId: Long) {
        viewModelScope.launch {
            repository.downvotePost(postId, userId)
        }
    }

    fun upvoteComment(commentId: Long, userId: Long) {
        viewModelScope.launch {
            repository.upvoteComment(commentId, userId)
        }
    }

    fun downvoteComment(commentId: Long, userId: Long) {
        viewModelScope.launch {
            repository.downvoteComment(commentId, userId)
        }
    }

    suspend fun getUserById(userId: Long): User? {
        return repository.getUserById(userId)
    }

    suspend fun getPostById(postId: Long): Post? {
        return repository.getPostById(postId)
    }
}
