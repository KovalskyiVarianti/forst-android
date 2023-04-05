package com.example.forst_android.map.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.forst_android.R
import com.example.forst_android.common.ui.viewBinding
import com.example.forst_android.databinding.FragmentMapBinding
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint

@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_map) {

    private val binding: FragmentMapBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.map.apply {
            setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
            setMultiTouchControls(true)
            controller.setZoom(15.0)
            controller.setCenter(GeoPoint(49.842957, 24.031111))
        }
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }
}