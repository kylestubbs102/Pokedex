package com.example.pokedex.domain.model

import com.example.pokedex.data.remote.dto.pokemoninfo.Stat

data class PokemonInfo(
    val id: Int,
    val name: String,
    val description: String,
    val category: String, // genus field in pokemon-species
    val types: List<String>,
    val imageUrl: String,
    val color: String,
    val isLiked: Boolean,
    val height: Int,    // deciliters
    val weight: Int,    // hectograms
    val genderRate: Int,   // in 8ths of female %, ex: 1 = 1/8 female
    val eggGroups: List<String>,
    val eggCycles: Int,
    val baseExperience: Int,
    val evolutionChainId: Int?,
)
