/*
 *  Copyright (C) 2017 Darel Bitsy
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.dbeginc.dbweather.utils.utility

import android.databinding.BindingAdapter
import android.os.Build
import android.support.design.widget.Snackbar
import android.support.graphics.drawable.VectorDrawableCompat
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.dbeginc.dbweather.R
import com.dbeginc.dbweather.utils.glide.BlurBackgroundTarget
import com.dbeginc.dbweather.utils.glide.BlurTransformation
import com.dbeginc.dbweather.utils.holder.ConstantHolder.YOUTUBE_THUMBNAIL_URL
import com.dbeginc.dbweatherweather.viewmodels.toFormattedTime
import java.util.*

/**
 * Created by Darel Bitsy on 26/04/17.
 * Custom Binder for my layout
 */

fun View.show() {
    visibility = View.VISIBLE
}

fun View.remove() {
    visibility = View.GONE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.isVisible() : Boolean = visibility == View.VISIBLE

fun View.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun ViewGroup.snack(message: String, duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(this, message, duration).show()
}

@BindingAdapter("setUpdateTime")
fun setWeatherUpdateTime(textView: TextView, time: Long) {
    textView.text = textView.context.getString(R.string.time_label).format(Locale.getDefault(), time.toFormattedTime(null))
}

@BindingAdapter("setImage")
fun setImageViewResource(imageView: ImageView, resource: Int) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        imageView.setImageDrawable(VectorDrawableCompat.create(imageView.resources, resource, imageView.context.theme))

    } else imageView.setImageResource(resource)
}

@BindingAdapter("setImageUrl")
fun setImage(imageView: ImageView, url: String?) {
    if (url != null && url.isNotEmpty()) {
        Glide.with(imageView)
                .load(url)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                .apply(RequestOptions.errorOf(R.drawable.no_image_icon))
                .apply(RequestOptions.centerCropTransform())
                .into(imageView)
    }
}

@BindingAdapter("setGifImage")
fun setGifImage(imageView: ImageView, gifImage: Int) {
    if (gifImage > 0) {
        Glide.with(imageView)
                .load(gifImage)
                .into(imageView)
    }
}


@BindingAdapter("setYoutubeImage")
fun setYoutubeImage(imageView: ImageView, url: String?) {
    if (url != null && url.isNotEmpty()) {
        Glide.with(imageView)
                .load(YOUTUBE_THUMBNAIL_URL.format(url))
                .apply(RequestOptions.errorOf(R.drawable.no_image_icon))
                .apply(RequestOptions.centerCropTransform())
                .into(imageView)
    }
}

@BindingAdapter("setBlurYoutubeBackgroundImage")
fun setBlurBackground(viewGroup: ViewGroup, url: String?) {
    if (url != null && url.isNotEmpty()) {
        Glide.with(viewGroup.context)
                .load(YOUTUBE_THUMBNAIL_URL.format(url))
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.bitmapTransform(BlurTransformation(viewGroup.context)))
                .into(BlurBackgroundTarget(viewGroup))
    }
}