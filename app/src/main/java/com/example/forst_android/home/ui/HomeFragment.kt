package com.example.forst_android.home.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.forst_android.R
import com.example.forst_android.common.ui.viewBinding
import com.example.forst_android.databinding.FragmentHomeBinding
import com.example.forst_android.home.navigation.HomeNavigationManager
import com.example.forst_android.main.navigation.MainNavigationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    @Inject
    lateinit var mainNavigationManager: MainNavigationManager

    @Inject
    lateinit var homeNavigationManager: HomeNavigationManager

    private val binding: FragmentHomeBinding by viewBinding()

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initNavigation()

        binding.accountIcon.setOnClickListener {
            mainNavigationManager.navigate(
                lifecycleScope,
                HomeFragmentDirections.actionMainFragmentToAccountFragment()
            )
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                homeViewModel.clusterDropdownData.collect { clusterData ->
                    binding.clusterDropdown.init(clusterData) {
                        mainNavigationManager.navigate(lifecycleScope, it)
                    }
                }
            }
        }
    }

    private fun initNavigation() {
        val navFragment = childFragmentManager.findFragmentById(R.id.mainFragmentContainer)
        val navController = (navFragment as NavHostFragment).navController
        binding.bottomNavigationBar.setupWithNavController(navController)

        lifecycleScope.launch {
            homeNavigationManager.getNavigationRequest()
                .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
                .collect { direction ->
                    navController.navigate(direction)
                }
        }
    }
}