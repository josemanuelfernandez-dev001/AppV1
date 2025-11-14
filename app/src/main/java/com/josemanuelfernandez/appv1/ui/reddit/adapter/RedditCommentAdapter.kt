package com.josemanuelfernandez.appv1.ui.reddit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
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

class RedditCommentAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val onUpvoteClick: (Comment) -> Unit,
    private val onDownvoteClick: (Comment) -> Unit,
    private val onReplyClick: (Comment) -> Unit,
    private val getReplies: (Long) -> LiveData<List<Comment>>,
    private val getUserById: suspend (Long) -> User?,
    private val nestLevel: Int = 0
) : ListAdapter<Comment, RedditCommentAdapter.CommentViewHolder>(CommentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reddit_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val indentView: View = itemView.findViewById(R.id.indentView)
        private val upvoteButton: ImageButton = itemView.findViewById(R.id.upvoteButton)
        private val downvoteButton: ImageButton = itemView.findViewById(R.id.downvoteButton)
        private val scoreText: TextView = itemView.findViewById(R.id.scoreText)
        private val authorText: TextView = itemView.findViewById(R.id.authorText)
        private val timestampText: TextView = itemView.findViewById(R.id.timestampText)
        private val contentText: TextView = itemView.findViewById(R.id.contentText)
        private val replyButton: MaterialButton = itemView.findViewById(R.id.replyButton)
        private val toggleRepliesButton: MaterialButton = itemView.findViewById(R.id.toggleRepliesButton)
        private val repliesRecyclerView: RecyclerView = itemView.findViewById(R.id.repliesRecyclerView)

        private var repliesAdapter: RedditCommentAdapter? = null
        private var repliesExpanded = false

        fun bind(comment: Comment) {
            // Set indent width based on nest level
            val indentWidth = nestLevel * 16 // 16dp per level
            val layoutParams = indentView.layoutParams
            layoutParams.width = (indentWidth * itemView.context.resources.displayMetrics.density).toInt()
            indentView.layoutParams = layoutParams

            contentText.text = comment.content
            scoreText.text = comment.likesCount.toString()
            timestampText.text = getTimeAgo(comment.timestamp)

            // Load user data asynchronously
            kotlinx.coroutines.MainScope().launch {
                getUserById(comment.userId)?.let { user ->
                    authorText.text = user.username
                }
            }

            upvoteButton.setOnClickListener {
                onUpvoteClick(comment)
            }

            downvoteButton.setOnClickListener {
                onDownvoteClick(comment)
            }

            replyButton.setOnClickListener {
                onReplyClick(comment)
            }

            // Setup replies if we're not too deeply nested
            if (nestLevel < 5) {
                getReplies(comment.id).observe(lifecycleOwner) { replies ->
                    if (replies.isNotEmpty()) {
                        toggleRepliesButton.visibility = View.VISIBLE
                        toggleRepliesButton.text = if (repliesExpanded) {
                            itemView.context.getString(R.string.collapse_thread)
                        } else {
                            itemView.context.getString(R.string.expand_thread)
                        }

                        if (repliesAdapter == null) {
                            repliesAdapter = RedditCommentAdapter(
                                lifecycleOwner,
                                onUpvoteClick,
                                onDownvoteClick,
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
                    toggleRepliesButton.text = if (repliesExpanded) {
                        itemView.context.getString(R.string.collapse_thread)
                    } else {
                        itemView.context.getString(R.string.expand_thread)
                    }
                }
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

    class CommentDiffCallback : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem == newItem
        }
    }
}
