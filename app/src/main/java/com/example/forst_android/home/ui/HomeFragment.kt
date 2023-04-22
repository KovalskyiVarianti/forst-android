package com.example.forst_android.home.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.forst_android.R
import com.example.forst_android.common.ui.viewBinding
import com.example.forst_android.databinding.FragmentHomeBinding
import com.example.forst_android.main.navigation.NavigationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    @Inject
    lateinit var navigationManager: NavigationManager

    private val binding: FragmentHomeBinding by viewBinding()

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navFragment = childFragmentManager.findFragmentById(R.id.mainFragmentContainer)
        (navFragment as? NavHostFragment)?.navController?.also { controller ->
            binding.bottomNavigationBar.setupWithNavController(controller)
        }

        binding.accountIcon.setOnClickListener {
            navigationManager.navigate(
                lifecycleScope,
                HomeFragmentDirections.actionMainFragmentToAccountFragment()
            )
        }




        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                homeViewModel.clusterDropdownData.collect { clusterData ->
                    binding.clusterDropdown.init(
                        binding.toolbar,
                        clusterData,
                        onShow = {
                            binding.accountIcon.setImageResource(R.drawable.icon_setting)
                            binding.accountIcon.setOnClickListener {
                                navigationManager.navigate(
                                    lifecycleScope,
                                    HomeFragmentDirections.actionMainFragmentToAccountFragment()
                                )
                            }
                        },
                        onHide = {
                            binding.accountIcon.setImageResource(R.drawable.icon_account)
                            binding.accountIcon.setOnClickListener {
                                navigationManager.navigate(
                                    lifecycleScope,
                                    HomeFragmentDirections.actionMainFragmentToAccountFragment()
                                )
                            }
                        },
                        onJoin = {
                            navigationManager.navigate(
                                lifecycleScope,
                                HomeFragmentDirections.actionHomeFragmentToClusterJoinFragment()
                            )
                        },
                        onCreate = {
                            navigationManager.navigate(
                                lifecycleScope,
                                HomeFragmentDirections.actionHomeFragmentToClusterCreateFragment()
                            )
                        },
                    )
                }
            }
        }
    }
}