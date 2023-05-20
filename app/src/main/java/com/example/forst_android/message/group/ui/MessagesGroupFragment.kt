package com.example.forst_android.message.group.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.forst_android.BuildConfig
import com.example.forst_android.R
import com.example.forst_android.common.ui.viewBinding
import com.example.forst_android.databinding.FragmentMessagesGroupBinding
import com.example.forst_android.message.group.ui.adapter.MessageGroupAdapter
import com.example.forst_android.message.ui.dialog.SendPhotoDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class MessagesGroupFragment : Fragment(R.layout.fragment_messages_group) {

    private val binding: FragmentMessagesGroupBinding by viewBinding()

    private val navArgs: MessagesGroupFragmentArgs by navArgs()

    private val messagesGroupViewModel: MessagesGroupViewModel by viewModels()

    private var sendPhotoDialog: SendPhotoDialog? = null

    private var lastPhoto: Uri? = null

    private val registerForImageResult =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                sendPhotoDialog?.dismiss()
                requireContext().contentResolver.openInputStream(uri)?.let { stream ->
                    messagesGroupViewModel.sendPhoto(
                        navArgs.groupId,
                        stream
                    )
                }
            }
        }

    private val registerForPhotoResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccessful ->
            if (isSuccessful) {
                lastPhoto?.let {
                    requireContext().contentResolver.openInputStream(it)?.let { stream ->
                        messagesGroupViewModel.sendPhoto(
                            navArgs.groupId,
                            stream
                        )
                    }
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sendPhotoDialog = SendPhotoDialog(requireContext())

        binding.messageList.adapter = MessageGroupAdapter()

        messagesGroupViewModel.fetchGroupInfo(navArgs.groupId)

        binding.apply {
            sendButton.setOnClickListener {
                messageInput.text.toString().takeIf { it.isNotBlank() }?.let {
                    messagesGroupViewModel.sendMessage(navArgs.groupId, it)
                }
                messageInput.setText("")
            }
            sendImageButton.setOnClickListener {
                sendPhotoDialog?.initDialog(
                    onPhoto = {
                        registerForPhotoResult.launch(
                            getTempFileUri()
                        )
                    },
                    onImageChoose = {
                        registerForImageResult.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                )
            }
            backButtonIcon.setOnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }

        lifecycleScope.launch {
            messagesGroupViewModel.getMessages(
                navArgs.groupId
            ).flowWithLifecycle(lifecycle, Lifecycle.State.CREATED).collect { messages ->
                (binding.messageList.adapter as MessageGroupAdapter).submitList(messages)
                if (messages.size > 1) {
                    binding.messageList.smoothScrollToPosition(messages.size - 1)
                }
            }
        }

        lifecycleScope.launch {
            messagesGroupViewModel.getGroupInfo().flowWithLifecycle(
                lifecycle,
                Lifecycle.State.CREATED
            ).collect { groupInfo ->
                binding.groupName.text = groupInfo?.name
            }
        }
    }

    private fun getTempFileUri(): Uri {
        return File.createTempFile("temp", null, requireContext().cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }.let {file ->
            FileProvider.getUriForFile(
                requireActivity().applicationContext,
                "${BuildConfig.APPLICATION_ID}.provider",
                file
            ).also { lastPhoto = it }
        }
    }
}