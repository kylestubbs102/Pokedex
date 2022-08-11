package com.example.pokedex.di

import android.app.Application
import androidx.room.Room
import com.example.pokedex.data.local.PokedexDao
import com.example.pokedex.data.local.PokedexDatabase
import com.example.pokedex.data.local.PokedexDatabase.Companion.DATABASE_NAME
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single { provideRoomDatabase(androidApplication()) }
    single { provideSectorDao(get()) }
}

fun provideRoomDatabase(
    app: Application
): PokedexDatabase = Room.databaseBuilder(
    app,
    PokedexDatabase::class.java,
    DATABASE_NAME
).build()

fun provideSectorDao(
    db: PokedexDatabase
): PokedexDao = db.pokedexDao