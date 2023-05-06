package com.example.forst_android.main.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.example.forst_android.R
import com.example.forst_android.databinding.ActivityMainBinding
import com.example.forst_android.main.navigation.MainNavigationManager
import com.example.forst_android.splash.ui.SplashFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@AndroidEntryPoint
@Singleton
class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private val mainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var mainNavigationManager: MainNavigationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }
        if (savedInstanceState == null) {
            initNavigation()
            lifecycleScope.launch {
                mainViewModel.getEntryPoint().collect { entryPoint ->
                    when (entryPoint) {
                        MainEntryPoint.ClusterEntry -> {
                            mainNavigationManager.navigate(
                                lifecycleScope,
                                SplashFragmentDirections.actionSplashFragmentToClusterEntryFragment()
                            )
                        }
                        MainEntryPoint.Home -> {
                            mainNavigationManager.navigate(
                                lifecycleScope,
                                SplashFragmentDirections.actionSplashFragmentToHomeFragment()
                            )
                        }
                        MainEntryPoint.Login -> {
                            mainNavigationManager.navigate(
                                lifecycleScope,
                                SplashFragmentDirections.actionSplashFragmentToAuthFragment()
                            )
                        }
                        MainEntryPoint.Splash -> {
                            // Already on the splash screen
                        }
                    }
                }
            }
        }
    }

    private fun initNavigation() {
        val navFragment = supportFragmentManager.findFragmentById(R.id.activityFragmentContainer)
        val navController = (navFragment as? NavHostFragment)?.navController
        lifecycleScope.launch {
            mainNavigationManager.getNavigationRequest()
                .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
                .collect { direction ->
                    navController?.navigate(direction)
                }
        }
    }
}