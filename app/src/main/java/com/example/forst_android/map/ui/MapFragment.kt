package com.example.forst_android.map.ui

import android.Manifest
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.forst_android.R
import com.example.forst_android.common.domain.service.UserService
import com.example.forst_android.common.ui.viewBinding
import com.example.forst_android.databinding.FragmentMapBinding
import com.example.forst_android.map.ui.adapter.MapFollowedAdapter
import com.example.forst_android.map.ui.adapter.MapFollowedItem
import com.example.forst_android.map.ui.dialog.AddPointDialog
import com.example.forst_android.map.ui.dialog.ClusterUserListDialogController
import com.example.forst_android.map.ui.dialog.ModeChangeAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import javax.inject.Inject


@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_map) {

    private val binding: FragmentMapBinding by viewBinding()

    private val mapViewModel: MapViewModel by viewModels()

    @Inject
    lateinit var userService: UserService

    private var myLocationOverlay: MyLocationNewOverlay? = null
    private var mapReceiverOverlay: MapEventsOverlay? = null
    private var clusterUserListDialogController: ClusterUserListDialogController? = null
    private var changeModeDialog: ModeChangeAlertDialog? = null
    private var addPointDialog: AddPointDialog? = null

    private val usersLocationsMarkers = mutableListOf<Marker>()
    private val pointsMarkers = mutableListOf<Marker>()

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
                    binding.map.controller.setZoom(15.0)
                }
            }
        )

        lifecycleScope.launch {
            mapViewModel.mapFollowedItems.flowWithLifecycle(
                lifecycle,
                Lifecycle.State.CREATED,
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
                                    R.color.primary
                                )
                            )
                            locationShareStateText.text =
                                context?.getString(R.string.sharing_location)
                        }
                        false -> {
                            locationShareStateButton.setCardBackgroundColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.surface
                                )
                            )
                            locationShareStateText.text =
                                context?.getString(R.string.location_is_hidden)
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            mapViewModel.getLocationCreateMode().flowWithLifecycle(
                lifecycle,
                Lifecycle.State.STARTED
            ).collect { state ->
                binding.addLocationButton.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        if (state) R.color.primary else R.color.surface
                    )
                )
            }
        }

        lifecycleScope.launch {
            mapViewModel.points.flowWithLifecycle(
                lifecycle,
                Lifecycle.State.STARTED
            ).collect { points ->
                binding.map.overlayManager.removeAll(pointsMarkers)
                val markers = points.map { point ->
                    Marker(binding.map).apply {
                        icon = getTextIcon(point.name)
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                        setVisible(true)
                        position = GeoPoint(point.lat, point.lng)
                    }
                }.also {
                    pointsMarkers.addAll(it)
                }

                binding.map.overlayManager.addAll(markers)
            }
        }

        binding.locationShareStateButton.setOnClickListener {
            mapViewModel.changeShareLocationState()
        }

        binding.addLocationButton.setOnClickListener {
            if (mapViewModel.getLocationCreateMode().value) {
                mapViewModel.updateCreationModeState(false)
                return@setOnClickListener
            }
            if (changeModeDialog == null) {
                changeModeDialog = ModeChangeAlertDialog(requireContext())
            }
            changeModeDialog?.show(
                onConfirmButton = {
                    mapViewModel.updateCreationModeState(true)
                }
            )
        }

        binding.map.apply {
            isHorizontalMapRepetitionEnabled = false
            isVerticalMapRepetitionEnabled = false
            setScrollableAreaLimitLatitude(
                MapView.getTileSystem().maxLatitude,
                MapView.getTileSystem().minLatitude,
                0
            )
            setScrollableAreaLimitLongitude(
                MapView.getTileSystem().minLongitude,
                MapView.getTileSystem().maxLongitude,
                0
            )
            controller.setCenter(GeoPoint(48.94, 31.42))
            maxZoomLevel = 20.0
            minZoomLevel = 3.0
            controller.setZoom(5.0)
            setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
            setMultiTouchControls(true)
            myLocationOverlay = MyLocationNewOverlay(
                GpsMyLocationProvider(context),
                this
            ).apply {
                loadMarkerImage(userService.photoUrl.orEmpty()) { resource ->
                    setDirectionIcon(resource)
                    setPersonIcon(resource)
                }
                setDirectionAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                setPersonAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                enableMyLocation()
                binding.myLocationButton.setOnClickListener {
                    controller.animateTo(myLocation)
                    controller.setCenter(myLocation)
                    controller.setZoom(15.0)
                }
            }
            mapReceiverOverlay = MapEventsOverlay(
                object : MapEventsReceiver {
                    override fun singleTapConfirmedHelper(p: GeoPoint) = false
                    override fun longPressHelper(p: GeoPoint): Boolean {
                        return mapViewModel.getLocationCreateMode().value.also { value ->
                            if (value) {
                                if (addPointDialog == null) {
                                    addPointDialog = AddPointDialog(requireContext())
                                }
                                addPointDialog?.show { name ->
                                    mapViewModel.createPoint(name, p.latitude, p.longitude)
                                }
                            }
                        }
                    }
                }
            )
            overlayManager.addAll(listOf(myLocationOverlay, mapReceiverOverlay))
        }
    }

    private fun addOverlay(userItems: List<MapFollowedItem.UserMapFollowedItem>) {
        binding.map.overlayManager.removeAll(usersLocationsMarkers)
        usersLocationsMarkers.clear()
        val markers = userItems.mapNotNull { userItem ->
            if (userItem.lat != null && userItem.lng != null) {
                Marker(binding.map).apply {
                    setOnMarkerClickListener { _, _ ->
                        binding.map.controller.animateTo(position)
                        binding.map.controller.setCenter(position)
                        binding.map.controller.setZoom(15.0)
                        showInfoWindow()
                        true
                    }
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                    id = userItem.id
                    title = userItem.name
                    setInfoWindow(MarkerInfoWindow(R.layout.view_map_detail_dialog, binding.map))
                    position = GeoPoint(userItem.lat, userItem.lng)
                    loadMarkerImage(userItem.imageUrl) { resource ->
                        resource.toDrawable(resources).let { drawable ->
                            icon = drawable
                            image = drawable
                        }
                    }
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

    private fun loadMarkerImage(photoUrl: String, onReady: (bitmap: Bitmap) -> Unit) {
        Glide.with(requireContext())
            .asBitmap()
            .override(100)
            .load(photoUrl)
            .circleCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .error(R.mipmap.ic_launcher_round)
            .into(
                object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        onReady(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // nothing
                    }
                }
            )
    }

    private fun getTextIcon(text: String): BitmapDrawable {
        val textColor = Paint().apply {
            color = ContextCompat.getColor(
                requireContext(),
                R.color.on_primary
            )
            textSize = 50f
            isAntiAlias = true
            typeface = Typeface.DEFAULT_BOLD
            textAlign = Paint.Align.LEFT
        }
        val bounds = Rect().apply {
            textColor.getTextBounds(
                text, 0, text.length, this
            )
        }
        return BitmapDrawable(
            resources,
            Bitmap.createBitmap(
                bounds.width(),
                bounds.height(),
                Bitmap.Config.ARGB_8888
            ).apply {
                Canvas(this).apply {
                    drawText(
                        text,
                        bounds.left.toFloat(),
                        bounds.height().toFloat(),
                        textColor
                    )
                }
            }
        )
    }
}