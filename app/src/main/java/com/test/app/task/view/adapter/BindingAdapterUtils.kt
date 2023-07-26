package com.test.app.task.view.adapter

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.test.app.R

object BindingAdapterUtils {

//    @JvmStatic
//    @BindingAdapter("app:updateVisibility")
//    fun updateVisibility(view: View, visible: Boolean) {
//        Log.e("Test","updateVisibility Caled")
//        view.visibility = if (visible) View.VISIBLE else View.GONE
//    }

    @JvmStatic
    @BindingAdapter("app:setImageSource")
    fun setImageSource(imgView: ImageView, imgUrl: String) {
        val imgUri =
            imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.baseline_image_24)
                    .error(R.drawable.baseline_broken_image_24))
            .into(imgView)
    }

}