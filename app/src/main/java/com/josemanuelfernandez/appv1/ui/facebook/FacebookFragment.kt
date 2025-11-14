package com.josemanuelfernandez.appv1.ui.facebook

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
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.josemanuelfernandez.appv1.R
import com.josemanuelfernandez.appv1.data.entity.Comment
import com.josemanuelfernandez.appv1.data.entity.User
import com.josemanuelfernandez.appv1.ui.facebook.adapter.FacebookCommentAdapter
import com.josemanuelfernandez.appv1.viewmodel.FacebookViewModel
import kotlinx.coroutines.launch

class FacebookFragment : Fragment() {
    private lateinit var viewModel: FacebookViewModel
    private lateinit var adapter: FacebookCommentAdapter
    private lateinit var commentsRecyclerView: RecyclerView
    private lateinit var commentInput: EditText
    private lateinit var userSpinner: Spinner
    private lateinit var postButton: MaterialButton
    
    private var users: List<User> = emptyList()
    private var replyingToComment: Comment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_facebook, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel = ViewModelProvider(this)[FacebookViewModel::class.java]
        
        commentsRecyclerView = view.findViewById(R.id.commentsRecyclerView)
        commentInput = view.findViewById(R.id.commentInput)
        userSpinner = view.findViewById(R.id.userSpinner)
        postButton = view.findViewById(R.id.postButton)

        setupRecyclerView()
        setupUserSpinner()
        setupPostButton()
        observeComments()
    }

    private fun setupRecyclerView() {
        adapter = FacebookCommentAdapter(
            lifecycleOwner = viewLifecycleOwner,
            onLikeClick = { comment ->
                viewModel.likeComment(comment.id)
            },
            onReplyClick = { comment ->
                showReplyDialog(comment)
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
            adapter = this@FacebookFragment.adapter
        }
    }

    private fun setupUserSpinner() {
        viewModel.allUsers.observe(viewLifecycleOwner) { userList ->
            users = userList
            val userNames = userList.map { it.displayName }
            val spinnerAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                userNames
            )
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            userSpinner.adapter = spinnerAdapter
        }
    }

    private fun setupPostButton() {
        postButton.setOnClickListener {
            val content = commentInput.text.toString().trim()
            if (content.isNotEmpty() && users.isNotEmpty()) {
                val selectedUserIndex = userSpinner.selectedItemPosition
                val selectedUser = users[selectedUserIndex]
                
                viewModel.addComment(
                    content = content,
                    userId = selectedUser.id,
                    parentCommentId = replyingToComment?.id
                )
                
                commentInput.text?.clear()
                replyingToComment = null
            }
        }
    }

    private fun observeComments() {
        viewModel.topLevelComments.observe(viewLifecycleOwner) { comments ->
            adapter.submitList(comments)
        }
    }

    private fun showReplyDialog(comment: Comment) {
        lifecycleScope.launch {
            val user = viewModel.getUserById(comment.userId)
            val dialogView = layoutInflater.inflate(R.layout.fragment_facebook, null)
            
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Responder a ${user?.displayName}")
                .setMessage(comment.content)
                .setView(dialogView)
                .setPositiveButton("Responder") { dialog, _ ->
                    replyingToComment = comment
                    dialog.dismiss()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }
}
