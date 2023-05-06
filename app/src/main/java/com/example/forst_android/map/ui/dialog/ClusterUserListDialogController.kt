package com.example.forst_android.map.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import com.example.forst_android.R
import com.example.forst_android.databinding.ViewMapUsersDialogBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class ClusterUserListDialogController(
    private val context: Context,
    private val onFollow: (userId: String, isFollowed: Boolean) -> Unit,
) {
    private var currentList: List<Unit>? = null
    private var dialogBinding: ViewMapUsersDialogBinding? = null
    private var bottomSheetDialog: BottomSheetDialog? = null

    private val adapter by lazy { ClusterUserMapAdapter(onFollow) }

    fun submitList(items: List<ClusterUserMapItem>) {
        adapter.submitList(items)
    }

    fun initDialog() {
        if (bottomSheetDialog == null) {
            bottomSheetDialog =
                BottomSheetDialog(context, R.style.AppBottomSheetDialogTheme).apply {
                    behavior.skipCollapsed = true
                }
            dialogBinding = ViewMapUsersDialogBinding.inflate(
                LayoutInflater.from(context),
                null,
                false
            ).apply {
                root.minHeight = getDialogHeight(context.resources.displayMetrics.heightPixels)
                bottomSheetDialog?.setContentView(root)
                userList.adapter = adapter
            }
        }
        bottomSheetDialog?.show()
        bottomSheetDialog?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun clearDialog() {
        bottomSheetDialog = null
        currentList = null
        dialogBinding = null
    }

    @Suppress("MagicNumber")
    private fun getDialogHeight(screenHeight: Int) = 2 * screenHeight / 3
}