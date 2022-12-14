package com.example.pokedex.di

import android.content.Context
import android.content.SharedPreferences
import com.example.pokedex.data.interfaces_impl.AppPreferencesImpl
import com.example.pokedex.data.interfaces_impl.PokedexRepositoryImpl
import com.example.pokedex.domain.interfaces.AppPreferences
import com.example.pokedex.domain.interfaces.AppPreferences.Companion.SHARED_PREF_NAME
import com.example.pokedex.domain.interfaces.PokedexRepository
import com.example.pokedex.domain.usecases.GetBaseStatsUseCase
import com.example.pokedex.domain.usecases.GetEvolutionChainUseCase
import com.example.pokedex.domain.usecases.GetPokemonAboutInfoUseCase
import com.example.pokedex.presentation.pokemondetail.PokemonDetailViewModel
import com.example.pokedex.presentation.pokemondetail.basestats.PokemonDetailBaseStatsViewModel
import com.example.pokedex.presentation.pokemondetail.evolution.PokemonDetailEvolutionViewModel
import com.example.pokedex.presentation.pokemonlist.PokemonListViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { provideSharedPreferences(androidContext()) }
    single<AppPreferences> { AppPreferencesImpl(get()) }

    single<PokedexRepository> { PokedexRepositoryImpl(get(), get()) }

    single { GetBaseStatsUseCase(get()) }
    single { GetEvolutionChainUseCase(get()) }
    single { GetPokemonAboutInfoUseCase(get()) }

    viewModel { PokemonDetailViewModel(get()) }
    viewModel { PokemonListViewModel(get(), get()) }
    viewModel { PokemonDetailBaseStatsViewModel(get()) }
    viewModel { PokemonDetailEvolutionViewModel(get()) }

    single { Dispatchers }
}

private fun provideSharedPreferences(
    context: Context
): SharedPreferences = context.getSharedPreferences(
    SHARED_PREF_NAME,
    Context.MODE_PRIVATE
)