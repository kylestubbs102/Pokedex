package com.example.pokedex.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.pokedex.data.interfaces_impl.ImageLoaderImpl
import com.example.pokedex.domain.interfaces.ImageLoader
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val imageLoaderModule = module {
    single { provideRequestOptions() }
    single { provideGlideInstance(androidContext(), get()) }
    single<ImageLoader> { ImageLoaderImpl(get()) }
}

private fun provideRequestOptions(): RequestOptions {
    return RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .error(com.google.android.material.R.drawable.mtrl_ic_error)
}

private fun provideGlideInstance(
    context: Context,
    requestOptions: RequestOptions,
): RequestManager = Glide
    .with(context)
    .setDefaultRequestOptions(requestOptions)
