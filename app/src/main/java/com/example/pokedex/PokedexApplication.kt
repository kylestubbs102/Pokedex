package com.example.pokedex

import android.app.Application
import com.example.pokedex.di.appModule
import com.example.pokedex.di.databaseModule
import com.example.pokedex.di.imageLoaderModule
import com.example.pokedex.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PokedexApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@PokedexApplication)
            modules(
                listOf(
                    appModule,
                    databaseModule,
                    networkModule,
                    imageLoaderModule,
                )
            )
        }
    }
}