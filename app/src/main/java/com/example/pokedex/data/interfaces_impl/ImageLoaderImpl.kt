package com.example.pokedex.data.interfaces_impl

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.RequestManager
import com.example.pokedex.domain.interfaces.ImageLoader

class ImageLoaderImpl(
    private val requestManager: RequestManager
) : ImageLoader {

    override fun loadImage(
        url: String,
        imageView: ImageView,
        placeholder: Drawable?
    ) {
        requestManager
            .load(url)
            .placeholder(placeholder)
            .thumbnail(
                requestManager
                    .asDrawable()
                    .sizeMultiplier(.1f)
            )
            .into(imageView)
    }
}