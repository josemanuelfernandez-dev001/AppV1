package com.josemanuelfernandez.appv1.data.repository

import androidx.lifecycle.LiveData
import com.josemanuelfernandez.appv1.data.dao.CommentDao
import com.josemanuelfernandez.appv1.data.dao.UserDao
import com.josemanuelfernandez.appv1.data.entity.Comment
import com.josemanuelfernandez.appv1.data.entity.CommentType
import com.josemanuelfernandez.appv1.data.entity.User

class FacebookRepository(
    private val userDao: UserDao,
    private val commentDao: CommentDao
) {
    fun getTopLevelComments(): LiveData<List<Comment>> {
        return commentDao.getTopLevelComments(CommentType.FACEBOOK)
    }

    fun getReplies(parentCommentId: Long): LiveData<List<Comment>> {
        return commentDao.getReplies(parentCommentId)
    }

    suspend fun getUserById(userId: Long): User? {
        return userDao.getUserById(userId)
    }

    suspend fun addComment(comment: Comment): Long {
        return commentDao.insert(comment)
    }

    suspend fun likeComment(commentId: Long) {
        commentDao.incrementLikes(commentId)
    }

    suspend fun unlikeComment(commentId: Long) {
        commentDao.decrementLikes(commentId)
    }

    fun getAllUsers(): LiveData<List<User>> {
        return userDao.getAllUsers()
    }
}
