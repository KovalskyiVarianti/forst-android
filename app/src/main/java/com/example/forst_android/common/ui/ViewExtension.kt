package com.example.forst_android.common.ui

import android.view.View
import androidx.core.view.isVisible

fun View.hide() {
    isVisible = false
}

fun View.show() {
    isVisible = true
}