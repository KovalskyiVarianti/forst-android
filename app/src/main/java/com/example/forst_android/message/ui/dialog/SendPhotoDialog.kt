package com.example.forst_android.message.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import com.example.forst_android.R
import com.example.forst_android.databinding.ViewSendPhotoDialogBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class SendPhotoDialog(
    private val context: Context,
) {

    private var bottomSheetDialog: BottomSheetDialog? = null
    private var dialogBinding: ViewSendPhotoDialogBinding? = null

    fun initDialog(
        onPhoto: () -> Unit,
        onImageChoose: () -> Unit,
    ) {
        if (bottomSheetDialog == null) {
            bottomSheetDialog =
                BottomSheetDialog(context, R.style.AppBottomSheetDialogTheme).apply {
                    behavior.skipCollapsed = true
                }
            dialogBinding = ViewSendPhotoDialogBinding.inflate(
                LayoutInflater.from(context),
                null,
                false
            ).apply {
                bottomSheetDialog?.setContentView(root)
                takePhotoIcon.setOnClickListener { onPhoto() }
                chooseFromGalleryIcon.setOnClickListener { onImageChoose() }
            }
        }
        bottomSheetDialog?.show()
        bottomSheetDialog?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun dismiss() {
        bottomSheetDialog?.dismiss()
    }
}