package com.example.pokedex.presentation.pokemonlist

import com.example.pokedex.domain.model.PokemonInfo

data class PokemonListState(
    val isLoading: Boolean = false,
    val pokemonList: List<PokemonInfo> = emptyList(),
    val errorMessage: String = "",
)
