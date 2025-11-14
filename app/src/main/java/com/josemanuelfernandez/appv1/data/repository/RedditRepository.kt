package com.josemanuelfernandez.appv1.data.repository

import androidx.lifecycle.LiveData
import com.josemanuelfernandez.appv1.data.dao.CommentDao
import com.josemanuelfernandez.appv1.data.dao.PostDao
import com.josemanuelfernandez.appv1.data.dao.UserDao
import com.josemanuelfernandez.appv1.data.dao.VoteDao
import com.josemanuelfernandez.appv1.data.entity.Comment
import com.josemanuelfernandez.appv1.data.entity.CommentType
import com.josemanuelfernandez.appv1.data.entity.Post
import com.josemanuelfernandez.appv1.data.entity.User
import com.josemanuelfernandez.appv1.data.entity.Vote

class RedditRepository(
    private val userDao: UserDao,
    private val postDao: PostDao,
    private val commentDao: CommentDao,
    private val voteDao: VoteDao
) {
    fun getAllPostsByScore(): LiveData<List<Post>> {
        return postDao.getAllPostsByScore()
    }

    fun getAllPostsByTime(): LiveData<List<Post>> {
        return postDao.getAllPostsByTime()
    }

    suspend fun getPostById(postId: Long): Post? {
        return postDao.getPostById(postId)
    }

    fun getPostComments(postId: Long): LiveData<List<Comment>> {
        return commentDao.getPostComments(postId)
    }

    fun getReplies(parentCommentId: Long): LiveData<List<Comment>> {
        return commentDao.getReplies(parentCommentId)
    }

    suspend fun getUserById(userId: Long): User? {
        return userDao.getUserById(userId)
    }

    suspend fun addPost(post: Post): Long {
        return postDao.insert(post)
    }

    suspend fun addComment(comment: Comment): Long {
        return commentDao.insert(comment)
    }

    suspend fun upvotePost(postId: Long, userId: Long) {
        val existingVote = voteDao.getPostVote(userId, postId)
        
        when {
            existingVote == null -> {
                // No vote exists, create upvote
                voteDao.insert(Vote(userId = userId, postId = postId, isUpvote = true))
                postDao.incrementUpvotes(postId)
            }
            existingVote.isUpvote -> {
                // Already upvoted, remove vote
                voteDao.delete(existingVote)
                postDao.decrementUpvotes(postId)
            }
            else -> {
                // Was downvoted, change to upvote
                voteDao.update(existingVote.copy(isUpvote = true))
                postDao.decrementDownvotes(postId)
                postDao.incrementUpvotes(postId)
            }
        }
    }

    suspend fun downvotePost(postId: Long, userId: Long) {
        val existingVote = voteDao.getPostVote(userId, postId)
        
        when {
            existingVote == null -> {
                // No vote exists, create downvote
                voteDao.insert(Vote(userId = userId, postId = postId, isUpvote = false))
                postDao.incrementDownvotes(postId)
            }
            !existingVote.isUpvote -> {
                // Already downvoted, remove vote
                voteDao.delete(existingVote)
                postDao.decrementDownvotes(postId)
            }
            else -> {
                // Was upvoted, change to downvote
                voteDao.update(existingVote.copy(isUpvote = false))
                postDao.decrementUpvotes(postId)
                postDao.incrementDownvotes(postId)
            }
        }
    }

    suspend fun getPostVote(postId: Long, userId: Long): Vote? {
        return voteDao.getPostVote(userId, postId)
    }

    suspend fun upvoteComment(commentId: Long, userId: Long) {
        val existingVote = voteDao.getCommentVote(userId, commentId)
        
        when {
            existingVote == null -> {
                voteDao.insert(Vote(userId = userId, commentId = commentId, isUpvote = true))
                commentDao.incrementLikes(commentId)
            }
            existingVote.isUpvote -> {
                voteDao.delete(existingVote)
                commentDao.decrementLikes(commentId)
            }
            else -> {
                voteDao.update(existingVote.copy(isUpvote = true))
                commentDao.incrementLikes(commentId)
                commentDao.incrementLikes(commentId)
            }
        }
    }

    suspend fun downvoteComment(commentId: Long, userId: Long) {
        val existingVote = voteDao.getCommentVote(userId, commentId)
        
        when {
            existingVote == null -> {
                voteDao.insert(Vote(userId = userId, commentId = commentId, isUpvote = false))
                commentDao.decrementLikes(commentId)
            }
            !existingVote.isUpvote -> {
                voteDao.delete(existingVote)
                commentDao.incrementLikes(commentId)
            }
            else -> {
                voteDao.update(existingVote.copy(isUpvote = false))
                commentDao.decrementLikes(commentId)
                commentDao.decrementLikes(commentId)
            }
        }
    }

    fun getAllUsers(): LiveData<List<User>> {
        return userDao.getAllUsers()
    }
}
