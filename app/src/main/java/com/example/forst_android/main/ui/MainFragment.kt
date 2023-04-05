package com.example.forst_android.main.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.forst_android.R
import com.example.forst_android.common.ui.viewBinding
import com.example.forst_android.databinding.FragmentMainBinding
import com.example.forst_android.main.navigation.NavigationManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    @Inject
    lateinit var navigationManager: NavigationManager

    private val binding: FragmentMainBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navFragment = childFragmentManager.findFragmentById(R.id.mainFragmentContainer)
        (navFragment as? NavHostFragment)?.navController?.also { controller ->
            binding.bottomNavigationBar.setupWithNavController(controller)
        }

        binding.accountIcon.setOnClickListener {
            navigationManager.navigate(
                lifecycleScope,
                MainFragmentDirections.actionMainFragmentToAccountFragment()
            )
        }
    }
}