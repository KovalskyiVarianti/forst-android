package com.example.forst_android.clusters.ui.dropdown

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.forst_android.R
import com.example.forst_android.databinding.ViewClustersPopupBinding

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
        anchor: View,
        clusterDropdownData: ClusterDropdownData,
        onShow: () -> Unit,
        onHide: () -> Unit,
        onJoin: () -> Unit,
        onCreate: () -> Unit,
    ) {
        text = clusterDropdownData.selectedCluster.name
        setOnClickListener {
            if (popupWindow == null) {
                popupWindow = PopupWindow(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    getDialogHeight(context.resources.displayMetrics.heightPixels)
                ).apply {
                    isOutsideTouchable = true
                    contentView = popupBinding.root
                    setOnDismissListener { onHide() }
                    setBackgroundDrawable(popupBackgroundDrawable)
                }
                popupBinding.createButton.setOnClickListener {
                    onCreate()
                    popupWindow?.dismiss()
                }
                popupBinding.joinButton.setOnClickListener {
                    onJoin()
                    popupWindow?.dismiss()
                }
                popupBinding.clusterList.apply {
                    adapter = ClusterPopupAdapter { id ->
                        clusterDropdownData.onSelected(id)
                        popupWindow?.dismiss()
                    }
                    addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
                }
            }
            (popupBinding.clusterList.adapter as? ClusterPopupAdapter)?.submitList(
                clusterDropdownData.otherClusters
            )
            if (popupWindow?.isShowing == true) {
                onHide()
                popupWindow?.dismiss()
            } else {
                onShow()
                popupWindow?.showAsDropDown(anchor)
            }
        }
    }

    private fun getDialogHeight(screenHeight: Int) = screenHeight / 2
}

