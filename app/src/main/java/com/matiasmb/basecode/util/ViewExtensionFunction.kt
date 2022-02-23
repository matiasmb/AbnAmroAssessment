package com.matiasmb.basecode.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImage(imageUrl: String, context: Context) {
    Glide.with(context)
        .load(imageUrl)
        .fitCenter()
        .error(android.R.drawable.ic_dialog_alert)
        .into(this)
}
