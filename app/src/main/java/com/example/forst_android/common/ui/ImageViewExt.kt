package com.example.forst_android.common.ui

import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.forst_android.R

fun ImageView.loadAvatar(imageUrl: String) = Glide.with(this)
    .load(imageUrl)
    .circleCrop()
    .diskCacheStrategy(DiskCacheStrategy.NONE)
    .skipMemoryCache(true)
    .error(ContextCompat.getDrawable(context, R.mipmap.ic_launcher_round))
    .into(this)
