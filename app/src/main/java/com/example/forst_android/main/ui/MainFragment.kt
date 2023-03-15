package com.example.forst_android.main.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.forst_android.R
import com.example.forst_android.common.ui.viewBinding
import com.example.forst_android.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val binding: FragmentMainBinding by viewBinding()
    private val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}