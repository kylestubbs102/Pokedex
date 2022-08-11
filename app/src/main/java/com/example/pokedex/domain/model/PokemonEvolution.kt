package com.example.pokedex.domain.model

data class PokemonEvolution(
    val evolutionChainId: Int,
    val id: Int,
    val evolvedSpeciesId: Int,
    val name: String,
    val evolvedSpeciesName: String,
    val minLevel: Int?,
)
