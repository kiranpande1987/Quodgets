package com.kprights.quodgets

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


/**
 * Copyright (c) 2021 for KPrights
 *
 * User : Kiran Pande
 * Date : 16/10/21
 * Time : 1:34 PM
 */

@BindingAdapter("showImage")
fun showImage(imageView: ImageView, icon: String?) {
    // var imageUrl = "http://openweathermap.org/img/wn/${icon}@4x.png"
    var imageUrl = "http://openweathermap.org/img/wn/10n@4x.png"

    imageUrl.let {
        val imgUri = imageUrl.toUri().buildUpon().scheme("http").build()

        Glide.with(imageView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_image)
                    .error(android.R.drawable.stat_notify_error)
            )
            .into(imageView)
    }
}