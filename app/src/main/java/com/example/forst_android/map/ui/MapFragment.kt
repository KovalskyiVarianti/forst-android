package com.example.forst_android.map.ui

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.forst_android.R
import com.example.forst_android.common.ui.viewBinding
import com.example.forst_android.databinding.FragmentMapBinding
import com.example.forst_android.map.ui.adapter.MapFollowedAdapter
import com.example.forst_android.map.ui.adapter.MapFollowedItem
import com.example.forst_android.map.ui.dialog.ClusterUserListDialogController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_map) {

    private val binding: FragmentMapBinding by viewBinding()

    private val mapViewModel: MapViewModel by viewModels()

    private var myLocationOverlay: MyLocationNewOverlay? = null
    private var clusterUserListDialogController: ClusterUserListDialogController? = null

    private val usersLocationsMarkers = mutableListOf<Marker>()

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
            }
            else -> {
                // No location access granted.
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clusterUserListDialogController =
            ClusterUserListDialogController(requireContext()) { userId, isFollowed ->
                mapViewModel.onFollow(userId, isFollowed)
            }
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        binding.followedUsers.adapter = MapFollowedAdapter(
            onAddFollowedUserListener = {
                clusterUserListDialogController?.initDialog()
            },
            onUserClick = { followedUserId ->
                usersLocationsMarkers.find { it.id == followedUserId }?.let { marker ->
                    binding.map.controller.animateTo(marker.position)
                    binding.map.controller.setCenter(marker.position)
                }
            }
        )

        lifecycleScope.launch {
            mapViewModel.mapFollowedItems.flowWithLifecycle(
                lifecycle,
                Lifecycle.State.STARTED,
            ).collect { followedItems ->
                addOverlay(followedItems.filterIsInstance<MapFollowedItem.UserMapFollowedItem>())
                (binding.followedUsers.adapter as MapFollowedAdapter).submitList(followedItems)
            }
        }

        lifecycleScope.launch {
            mapViewModel.clusterMapUsers.flowWithLifecycle(
                lifecycle,
                Lifecycle.State.STARTED
            ).collect { users ->
                clusterUserListDialogController?.submitList(users)
            }
        }

        lifecycleScope.launch {
            mapViewModel.locationShareState.flowWithLifecycle(
                lifecycle,
                Lifecycle.State.STARTED
            ).collect { shareState ->
                binding.apply {
                    when (shareState) {
                        true -> {
                            locationShareStateButton.setCardBackgroundColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.on_primary
                                )
                            )
                            locationShareStateText.text =
                                context?.getString(R.string.sharing_location)
                        }
                        false -> {
                            locationShareStateButton.setCardBackgroundColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.on_primary_variant
                                )
                            )
                            locationShareStateText.text =
                                context?.getString(R.string.location_is_hidden)
                        }
                    }
                }
            }
        }

        binding.locationShareStateButton.setOnClickListener {
            mapViewModel.changeShareLocationState()
        }

        binding.map.apply {
            setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
            setMultiTouchControls(true)
            controller.setZoom(15.0)
            myLocationOverlay = MyLocationNewOverlay(
                GpsMyLocationProvider(context),
                this
            ).apply {
                setPersonIcon(
                    AppCompatResources.getDrawable(context, R.drawable.icon_account)
                        ?.toBitmap(100, 100)
                )
                enableMyLocation()
                binding.myLocationButton.setOnClickListener {
                    controller.setCenter(myLocation)
                    controller.setZoom(15.0)
                }
            }
            overlayManager.add(myLocationOverlay)
        }
    }

    private fun addOverlay(userItems: List<MapFollowedItem.UserMapFollowedItem>) {
        binding.map.overlayManager.removeAll(usersLocationsMarkers)
        usersLocationsMarkers.clear()
        val markers = userItems.mapNotNull { userItem ->
            if (userItem.lat != null && userItem.lng != null) {
                Marker(binding.map).apply {
                    id = userItem.id
                    title = userItem.name
                    position = GeoPoint(userItem.lat, userItem.lng)
                    icon = ContextCompat.getDrawable(requireContext(), R.mipmap.ic_launcher_round)
                    subDescription = userItem.lastUpdate
                }
            } else {
                null
            }
        }.also {
            usersLocationsMarkers.addAll(it)
        }
        binding.map.overlayManager?.addAll(markers)
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