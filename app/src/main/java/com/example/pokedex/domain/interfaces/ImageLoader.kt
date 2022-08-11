package com.example.pokedex.domain.interfaces

import android.graphics.drawable.Drawable
import android.widget.ImageView

interface ImageLoader {
    fun loadImage(
        url: String,
        imageView: ImageView,
        placeholder: Drawable? = null
    )
}