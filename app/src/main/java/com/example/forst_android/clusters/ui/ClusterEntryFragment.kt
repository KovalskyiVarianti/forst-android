package com.example.forst_android.clusters.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.forst_android.R
import com.example.forst_android.common.ui.viewBinding
import com.example.forst_android.databinding.FragmentClusterEntryBinding
import com.example.forst_android.main.navigation.NavigationManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ClusterEntryFragment : Fragment(R.layout.fragment_cluster_entry) {

    private val binding: FragmentClusterEntryBinding by viewBinding()

    @Inject
    lateinit var navigationManager: NavigationManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            createButton.setOnClickListener {
                navigationManager.navigate(
                    lifecycleScope,
                    ClusterEntryFragmentDirections.actionClusterEntryFragmentToClusterCreateFragment()
                )
            }
            joinButton.setOnClickListener {
                navigationManager.navigate(
                    lifecycleScope,
                    ClusterEntryFragmentDirections.actionClusterEntryFragmentToClusterJoinFragment()
                )
            }
        }
    }
}