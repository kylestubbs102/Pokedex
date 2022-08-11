package com.example.pokedex.di

import android.content.Context
import android.content.SharedPreferences
import com.example.pokedex.data.preferences.AppPreferencesImpl
import com.example.pokedex.data.repository.PokedexRepositoryImpl
import com.example.pokedex.domain.preferences.AppPreferences
import com.example.pokedex.domain.preferences.AppPreferences.Companion.SHARED_PREF_NAME
import com.example.pokedex.domain.repository.PokedexRepository
import com.example.pokedex.domain.usecases.GetBaseStatsUseCase
import com.example.pokedex.presentation.pokemoncardlist.PokemonCardListViewModel
import com.example.pokedex.presentation.pokemondetail.PokemonDetailViewModel
import com.example.pokedex.presentation.pokemondetail.basestats.PokemonDetailBaseStatsViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { provideSharedPreferences(androidContext()) }
    single<AppPreferences> { AppPreferencesImpl(get()) }

    single<PokedexRepository> { PokedexRepositoryImpl(get(), get()) }

    single { GetBaseStatsUseCase(get()) }

    viewModel { PokemonDetailViewModel(get()) }
    viewModel { PokemonCardListViewModel(get()) }
    viewModel { PokemonDetailBaseStatsViewModel(get()) }

    single { Dispatchers }
}

fun provideSharedPreferences(
    context: Context
): SharedPreferences = context.getSharedPreferences(
    SHARED_PREF_NAME,
    Context.MODE_PRIVATE
)