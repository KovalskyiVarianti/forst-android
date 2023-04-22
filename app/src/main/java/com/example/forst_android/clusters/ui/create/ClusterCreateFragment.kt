package com.example.forst_android.clusters.ui.create

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.forst_android.R
import com.example.forst_android.common.ui.viewBinding
import com.example.forst_android.databinding.FragmentClusterCreateBinding
import com.example.forst_android.main.navigation.NavigationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ClusterCreateFragment : Fragment(R.layout.fragment_cluster_create) {

    private val binding: FragmentClusterCreateBinding by viewBinding()

    private val clusterCreateViewModel: ClusterCreateViewModel by viewModels()

    @Inject
    lateinit var navigationManager: NavigationManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.clusterCreateButton.setOnClickListener {
            val clusterName = binding.clusterNameInput.text?.toString()
            clusterCreateViewModel.createCluster(clusterName)
        }

        lifecycleScope.launch {
            clusterCreateViewModel.getClusterCreateResult()
                .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED).collect { createState ->
                    when (createState) {
                        ClusterCreateState.Creating -> {
                            // nothing
                        }
                        is ClusterCreateState.Error -> {
                            binding.clusterNameLayout.error = createState.message
                        }
                        ClusterCreateState.Success -> {
                            navigationManager.navigate(
                                lifecycleScope,
                                ClusterCreateFragmentDirections.actionClusterCreateFragmentToHomeFragment()
                            )
                        }
                    }
                }
        }

        binding.clusterNameInput.addTextChangedListener { binding.clusterNameLayout.error = null }
    }
}