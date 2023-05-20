package com.example.forst_android.map.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.forst_android.R
import com.example.forst_android.databinding.ViewModeChangeDialogBinding

class ModeChangeAlertDialog(val context: Context) {

    private val binding = ViewModeChangeDialogBinding.inflate(LayoutInflater.from(context))
    private var dialog = AlertDialog.Builder(context, R.style.AppBottomSheetDialogTheme)
        .setView(binding.root).create()

    fun show(onConfirmButton: View.OnClickListener? = null) {
        if (dialog.isShowing) {
            return
        }
        binding.apply {
            cancelButton.setOnClickListener {
                dialog.dismiss()
            }
            confirmButton.setOnClickListener {
                dialog.dismiss()
                onConfirmButton?.onClick(it)
            }
        }
        dialog.show()
    }
}