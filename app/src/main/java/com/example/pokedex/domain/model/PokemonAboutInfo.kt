package com.example.pokedex.domain.model

data class PokemonAboutInfo(
    val genus: String,
    val description: String,
    val height: Int,
    val weight: Int,
    val genderRate: Int,
    val eggGroups: List<String>,
    val eggCycles: Int,
    val baseExperience: Int,
)
