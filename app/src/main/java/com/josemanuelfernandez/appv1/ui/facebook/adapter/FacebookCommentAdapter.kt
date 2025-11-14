package com.josemanuelfernandez.appv1.ui.facebook.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.josemanuelfernandez.appv1.R
import com.josemanuelfernandez.appv1.data.entity.Comment
import com.josemanuelfernandez.appv1.data.entity.User
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class FacebookCommentAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val onLikeClick: (Comment) -> Unit,
    private val onReplyClick: (Comment) -> Unit,
    private val getReplies: (Long) -> LiveData<List<Comment>>,
    private val getUserById: suspend (Long) -> User?,
    private val nestLevel: Int = 0
) : ListAdapter<Comment, FacebookCommentAdapter.CommentViewHolder>(CommentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_facebook_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val avatarText: TextView = itemView.findViewById(R.id.avatarText)
        private val usernameText: TextView = itemView.findViewById(R.id.usernameText)
        private val timestampText: TextView = itemView.findViewById(R.id.timestampText)
        private val contentText: TextView = itemView.findViewById(R.id.contentText)
        private val likeButton: MaterialButton = itemView.findViewById(R.id.likeButton)
        private val likesCountText: TextView = itemView.findViewById(R.id.likesCountText)
        private val replyButton: MaterialButton = itemView.findViewById(R.id.replyButton)
        private val toggleRepliesButton: MaterialButton = itemView.findViewById(R.id.toggleRepliesButton)
        private val repliesRecyclerView: RecyclerView = itemView.findViewById(R.id.repliesRecyclerView)

        private var repliesAdapter: FacebookCommentAdapter? = null
        private var repliesExpanded = false

        fun bind(comment: Comment) {
            contentText.text = comment.content
            timestampText.text = getTimeAgo(comment.timestamp)
            likesCountText.text = itemView.context.getString(R.string.likes_count, comment.likesCount)

            // Load user data asynchronously
            kotlinx.coroutines.MainScope().launch {
                getUserById(comment.userId)?.let { user ->
                    usernameText.text = user.displayName
                    avatarText.text = user.avatarUrl ?: "👤"
                }
            }

            likeButton.setOnClickListener {
                onLikeClick(comment)
            }

            replyButton.setOnClickListener {
                onReplyClick(comment)
            }

            // Setup replies if we're not too deeply nested
            if (nestLevel < 3) {
                getReplies(comment.id).observe(lifecycleOwner) { replies ->
                    if (replies.isNotEmpty()) {
                        toggleRepliesButton.visibility = View.VISIBLE
                        toggleRepliesButton.text = if (repliesExpanded) {
                            itemView.context.getString(R.string.hide_replies)
                        } else {
                            itemView.context.getString(R.string.show_replies, replies.size)
                        }

                        if (repliesAdapter == null) {
                            repliesAdapter = FacebookCommentAdapter(
                                lifecycleOwner,
                                onLikeClick,
                                onReplyClick,
                                getReplies,
                                getUserById,
                                nestLevel + 1
                            )
                            repliesRecyclerView.apply {
                                layoutManager = LinearLayoutManager(context)
                                adapter = repliesAdapter
                            }
                        }
                        repliesAdapter?.submitList(replies)
                    } else {
                        toggleRepliesButton.visibility = View.GONE
                    }
                }

                toggleRepliesButton.setOnClickListener {
                    repliesExpanded = !repliesExpanded
                    repliesRecyclerView.visibility = if (repliesExpanded) View.VISIBLE else View.GONE
                    getReplies(comment.id).value?.let { replies ->
                        toggleRepliesButton.text = if (repliesExpanded) {
                            itemView.context.getString(R.string.hide_replies)
                        } else {
                            itemView.context.getString(R.string.show_replies, replies.size)
                        }
                    }
                }
            }
        }

        private fun getTimeAgo(timestamp: Long): String {
            val now = System.currentTimeMillis()
            val diff = now - timestamp
            
            return when {
                diff < TimeUnit.MINUTES.toMillis(1) -> "Ahora"
                diff < TimeUnit.HOURS.toMillis(1) -> {
                    val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
                    "hace ${minutes}m"
                }
                diff < TimeUnit.DAYS.toMillis(1) -> {
                    val hours = TimeUnit.MILLISECONDS.toHours(diff)
                    "hace ${hours}h"
                }
                diff < TimeUnit.DAYS.toMillis(7) -> {
                    val days = TimeUnit.MILLISECONDS.toDays(diff)
                    "hace ${days}d"
                }
                else -> {
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(timestamp))
                }
            }
        }
    }

    class CommentDiffCallback : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem == newItem
        }
    }
}
