package com.example.forst_android.clusters.ui.dropdown

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.navigation.NavDirections
import com.example.forst_android.R
import com.example.forst_android.clusters.ui.dropdown.adapter.ClusterPopupAdapter
import com.example.forst_android.common.ui.ItemClickListener
import com.example.forst_android.databinding.ViewClustersPopupBinding
import com.example.forst_android.home.ui.HomeFragmentDirections

class ClusterDropdown @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    private var popupWindow: PopupWindow? = null

    private val popupBinding = ViewClustersPopupBinding.inflate(LayoutInflater.from(context))

    private val popupBackgroundDrawable = GradientDrawable().apply {
        setColor(context.getColor(R.color.primary))
        cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, 60f, 60f, 60f, 60f)
    }

    fun init(
        clusterDropdownData: ClusterDropdownData,
        onNavigated: (navDirections: NavDirections) -> Unit
    ) {
        text = clusterDropdownData.selectedCluster.name
        popupBinding.selectedClusterName.text = clusterDropdownData.selectedCluster.name
        popupBinding.clusterListTitle.isVisible = clusterDropdownData.otherClusters.isNotEmpty()
        setOnClickListener {
            if (popupWindow == null) {
                popupWindow = PopupWindow(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                ).apply {
                    isOutsideTouchable = true
                    contentView = setupPopupBinding(
                        clusterDropdownData.onSelected,
                        onNavigated
                    )
                    setBackgroundDrawable(popupBackgroundDrawable)
                }
            }
            popupBinding.settingsIcon.isInvisible =
                clusterDropdownData.selectedCluster.isOwner.not()

            (popupBinding.clusterList.adapter as? ClusterPopupAdapter)?.submitList(
                clusterDropdownData.otherClusters
            )
            popupWindow?.showAtLocation(this, Gravity.TOP, 0, 0)
        }
    }

    private fun setupPopupBinding(
        onSelected: ItemClickListener,
        onNavigated: (navDirections: NavDirections) -> Unit
    ) = popupBinding.apply {
        addButton.setOnClickListener {
            onNavigated(
                HomeFragmentDirections.actionHomeFragmentToClusterEntryFragment()
            )
            popupWindow?.dismiss()
        }
        clusterList.apply {
            adapter = ClusterPopupAdapter { id ->
                onSelected(id)
                popupWindow?.dismiss()
            }
            maxHeight = getDialogHeight(context.resources.displayMetrics.heightPixels)
        }
        settingsIcon.setOnClickListener {
            popupWindow?.dismiss()
            onNavigated(HomeFragmentDirections.actionHomeFragmentToSettingsFragment())
        }
        selectedClusterName.setOnClickListener { popupWindow?.dismiss() }
    }.root

    private fun getDialogHeight(screenHeight: Int) = screenHeight / 2
}

