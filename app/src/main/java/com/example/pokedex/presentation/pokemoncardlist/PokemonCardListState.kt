package com.example.pokedex.presentation.pokemoncardlist

import com.example.pokedex.domain.model.PokemonCardInfo

data class PokemonCardListState(
    val isLoading: Boolean = false,
    val pokemonCardList: List<PokemonCardInfo> = emptyList(),
    val errorMessage: String = "",
)
