package com.example.myrecipeapp.data

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.myrecipeapp.R

object ImageLoader {
    private const val BASE_URL = "https://recipes.androidsprint.ru/api/"
    private const val IMAGE_PATH = "images/"

    fun loadImage(context: Context, imageName: String, target: ImageView) {
        val fullUrl = BASE_URL + IMAGE_PATH + imageName

        Glide.with(context)
            .load(fullUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(target)
    }
}