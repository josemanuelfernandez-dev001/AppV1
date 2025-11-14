package com.josemanuelfernandez.appv1.ui.reddit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.josemanuelfernandez.appv1.R
import com.josemanuelfernandez.appv1.data.entity.Comment
import com.josemanuelfernandez.appv1.data.entity.Post
import com.josemanuelfernandez.appv1.data.entity.User
import com.josemanuelfernandez.appv1.ui.reddit.adapter.RedditCommentAdapter
import com.josemanuelfernandez.appv1.ui.reddit.adapter.RedditPostAdapter
import com.josemanuelfernandez.appv1.viewmodel.RedditViewModel
import kotlinx.coroutines.launch

class RedditFragment : Fragment() {
    private lateinit var viewModel: RedditViewModel
    private lateinit var postAdapter: RedditPostAdapter
    private lateinit var postsRecyclerView: RecyclerView
    private lateinit var createPostButton: FloatingActionButton
    
    private var users: List<User> = emptyList()
    private val currentUserId = 1L // Default user for this demo

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reddit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel = ViewModelProvider(this)[RedditViewModel::class.java]
        
        postsRecyclerView = view.findViewById(R.id.postsRecyclerView)
        createPostButton = view.findViewById(R.id.createPostButton)

        setupRecyclerView()
        setupCreatePostButton()
        observePosts()
        observeUsers()
    }

    private fun setupRecyclerView() {
        postAdapter = RedditPostAdapter(
            onUpvoteClick = { post ->
                viewModel.upvotePost(post.id, currentUserId)
            },
            onDownvoteClick = { post ->
                viewModel.downvotePost(post.id, currentUserId)
            },
            onCommentsClick = { post ->
                showCommentsDialog(post)
            },
            getUserById = { userId ->
                viewModel.getUserById(userId)
            }
        )

        postsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = postAdapter
        }
    }

    private fun setupCreatePostButton() {
        createPostButton.setOnClickListener {
            showCreatePostDialog()
        }
    }

    private fun observePosts() {
        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            postAdapter.submitList(posts)
        }
    }

    private fun observeUsers() {
        viewModel.allUsers.observe(viewLifecycleOwner) { userList ->
            users = userList
        }
    }

    private fun showCreatePostDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_create_post, null)
        val titleInput = dialogView.findViewById<TextInputEditText>(R.id.titleInput)
        val contentInput = dialogView.findViewById<TextInputEditText>(R.id.contentInput)
        val userSpinner = dialogView.findViewById<Spinner>(R.id.userSpinner)

        // Setup user spinner
        val userNames = users.map { it.displayName }
        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            userNames
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        userSpinner.adapter = spinnerAdapter

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Crear nuevo post")
            .setView(dialogView)
            .setPositiveButton("Crear") { dialog, _ ->
                val title = titleInput.text.toString().trim()
                val content = contentInput.text.toString().trim()
                
                if (title.isNotEmpty() && users.isNotEmpty()) {
                    val selectedUserIndex = userSpinner.selectedItemPosition
                    val selectedUser = users[selectedUserIndex]
                    
                    viewModel.addPost(
                        title = title,
                        content = content,
                        userId = selectedUser.id
                    )
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun showCommentsDialog(post: Post) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_post_comments, null)
        val commentsRecyclerView = dialogView.findViewById<RecyclerView>(R.id.commentsRecyclerView)
        val commentInput = dialogView.findViewById<EditText>(R.id.commentInput)
        val postCommentButton = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.postCommentButton)

        val commentAdapter = RedditCommentAdapter(
            lifecycleOwner = viewLifecycleOwner,
            onUpvoteClick = { comment ->
                viewModel.upvoteComment(comment.id, currentUserId)
            },
            onDownvoteClick = { comment ->
                viewModel.downvoteComment(comment.id, currentUserId)
            },
            onReplyClick = { comment ->
                showReplyDialog(post, comment)
            },
            getReplies = { parentId ->
                viewModel.getReplies(parentId)
            },
            getUserById = { userId ->
                viewModel.getUserById(userId)
            }
        )

        commentsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = commentAdapter
        }

        viewModel.getPostComments(post.id).observe(viewLifecycleOwner) { comments ->
            commentAdapter.submitList(comments)
        }

        postCommentButton.setOnClickListener {
            val content = commentInput.text.toString().trim()
            if (content.isNotEmpty()) {
                viewModel.addComment(
                    content = content,
                    postId = post.id,
                    userId = currentUserId
                )
                commentInput.text?.clear()
            }
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(post.title)
            .setView(dialogView)
            .setPositiveButton("Cerrar", null)
            .show()
    }

    private fun showReplyDialog(post: Post, parentComment: Comment) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_reply_comment, null)
        val replyInput = dialogView.findViewById<EditText>(R.id.replyInput)

        lifecycleScope.launch {
            val user = viewModel.getUserById(parentComment.userId)
            
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Responder a ${user?.displayName}")
                .setMessage(parentComment.content)
                .setView(dialogView)
                .setPositiveButton("Responder") { dialog, _ ->
                    val content = replyInput.text.toString().trim()
                    if (content.isNotEmpty()) {
                        viewModel.addComment(
                            content = content,
                            postId = post.id,
                            userId = currentUserId,
                            parentCommentId = parentComment.id
                        )
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }
}
