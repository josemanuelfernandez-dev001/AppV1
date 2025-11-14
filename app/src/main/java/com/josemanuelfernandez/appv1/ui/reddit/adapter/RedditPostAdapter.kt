package com.josemanuelfernandez.appv1.ui.reddit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.josemanuelfernandez.appv1.R
import com.josemanuelfernandez.appv1.data.entity.Post
import com.josemanuelfernandez.appv1.data.entity.User
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class RedditPostAdapter(
    private val onUpvoteClick: (Post) -> Unit,
    private val onDownvoteClick: (Post) -> Unit,
    private val onCommentsClick: (Post) -> Unit,
    private val getUserById: suspend (Long) -> User?
) : ListAdapter<Post, RedditPostAdapter.PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reddit_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val upvoteButton: ImageButton = itemView.findViewById(R.id.upvoteButton)
        private val downvoteButton: ImageButton = itemView.findViewById(R.id.downvoteButton)
        private val scoreText: TextView = itemView.findViewById(R.id.scoreText)
        private val titleText: TextView = itemView.findViewById(R.id.titleText)
        private val authorText: TextView = itemView.findViewById(R.id.authorText)
        private val timestampText: TextView = itemView.findViewById(R.id.timestampText)
        private val contentText: TextView = itemView.findViewById(R.id.contentText)
        private val commentsButton: MaterialButton = itemView.findViewById(R.id.commentsButton)

        fun bind(post: Post) {
            titleText.text = post.title
            contentText.text = post.content
            scoreText.text = post.score.toString()
            timestampText.text = getTimeAgo(post.timestamp)

            // Load user data asynchronously
            kotlinx.coroutines.MainScope().launch {
                getUserById(post.userId)?.let { user ->
                    authorText.text = itemView.context.getString(R.string.posted_by, user.username)
                }
            }

            upvoteButton.setOnClickListener {
                onUpvoteClick(post)
            }

            downvoteButton.setOnClickListener {
                onDownvoteClick(post)
            }

            commentsButton.setOnClickListener {
                onCommentsClick(post)
            }
        }

        private fun getTimeAgo(timestamp: Long): String {
            val now = System.currentTimeMillis()
            val diff = now - timestamp
            
            return when {
                diff < TimeUnit.MINUTES.toMillis(1) -> "ahora"
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

    class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }
}
