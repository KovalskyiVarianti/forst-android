package com.example.forst_android.settings.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.forst_android.R
import com.example.forst_android.common.ui.viewBinding
import com.example.forst_android.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val binding: FragmentSettingsBinding by viewBinding()

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        binding.backButtonIcon.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        binding.clusterId.setOnClickListener {
            copyToClipboard(binding.clusterId.text.toString())
        }

        lifecycleScope.launch {
            settingsViewModel.clusterInfo.flowWithLifecycle(
                lifecycle,
                Lifecycle.State.CREATED
            ).collect { clusterInfo ->
                binding.apply {
                    clusterId.setText(clusterInfo.id)
                    clusterName.text = clusterInfo.name
                    clusterPrivacyStateButton.isChecked = clusterInfo.isPrivate
                    clusterPrivacyStateButton.setOnCheckedChangeListener { _, isChecked ->
                        settingsViewModel.updateClusterPrivacy(isChecked)
                    }
                }
            }
        }
    }

    private fun copyToClipboard(text: CharSequence) {
        val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Cluster id", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Cluster id is copied", Toast.LENGTH_LONG).show()
    }
}