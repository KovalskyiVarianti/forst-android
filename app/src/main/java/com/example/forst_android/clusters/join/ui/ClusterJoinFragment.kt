package com.example.forst_android.clusters.join.ui

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.forst_android.R
import com.example.forst_android.clusters.join.ui.adapter.ClusterJoinAdapter
import com.example.forst_android.common.ui.viewBinding
import com.example.forst_android.databinding.FragmentClusterJoinBinding
import com.example.forst_android.main.navigation.MainNavigationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ClusterJoinFragment : Fragment(R.layout.fragment_cluster_join) {

    private val binding: FragmentClusterJoinBinding by viewBinding()

    private val clusterJoinViewModel: ClusterJoinViewModel by viewModels()

    @Inject
    lateinit var mainNavigationManager: MainNavigationManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.clusterList.adapter = ClusterJoinAdapter { id ->
            binding.clusterIdInput.setText(id)
        }
        binding.clusterJoinButton.setOnClickListener {
            clusterJoinViewModel.joinCluster(binding.clusterIdInput.text?.toString())
        }

        lifecycleScope.launch {
            clusterJoinViewModel.getClusterJoinResult().flowWithLifecycle(
                lifecycle,
                Lifecycle.State.CREATED
            ).collect { createState ->
                when (createState) {
                    ClusterJoinState.Joining -> {
                        // nothing
                    }
                    is ClusterJoinState.Error -> {
                        binding.clusterIdLayout.error = createState.message
                    }
                    ClusterJoinState.Success -> {
                        mainNavigationManager.navigate(
                            lifecycleScope,
                            ClusterJoinFragmentDirections.actionClusterJoinFragmentToHomeFragment()
                        )
                    }
                }
            }
        }

        lifecycleScope.launch {
            clusterJoinViewModel.getAllClusters().collect { clusters ->
                (binding.clusterList.adapter as? ClusterJoinAdapter)?.submitList(clusters)
            }
        }

        binding.clusterIdInput.addTextChangedListener { binding.clusterIdLayout.error = null }
    }
}