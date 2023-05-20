package com.example.forst_android.map.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.example.forst_android.R
import com.example.forst_android.databinding.ViewAddLocationDialogBinding

class AddPointDialog(val context: Context) {

    private val binding = ViewAddLocationDialogBinding.inflate(LayoutInflater.from(context))
    private var dialog = AlertDialog.Builder(context, R.style.AppBottomSheetDialogTheme)
        .setView(binding.root).create()

    fun show(onConfirmButton: (name: String) -> Unit) {
        if (dialog.isShowing) {
            return
        }
        binding.apply {
            cancelButton.setOnClickListener {
                dialog.dismiss()
            }
            confirmButton.setOnClickListener {
                val pointName = pointNameInput.text?.toString().orEmpty()
                if (pointName.isNotBlank()) {
                    onConfirmButton(pointName)
                    dialog.dismiss()
                    pointNameInput.setText("")
                } else {
                    pointNameLayout.error = "Incorrect point name"
                }
            }
            pointNameInput.addTextChangedListener { pointNameLayout.error = null }
        }
        dialog.show()
    }
}