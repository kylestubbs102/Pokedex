package com.example.pokedex.domain.model

data class BaseStat(
    val statId: Int,
    val pokemonId: Int,
    val name: String,
    val value: Int,
)
