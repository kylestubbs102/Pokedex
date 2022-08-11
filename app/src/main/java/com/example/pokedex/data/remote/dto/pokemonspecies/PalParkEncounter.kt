package com.example.pokedex.data.remote.dto.pokemonspecies


import com.fasterxml.jackson.annotation.JsonProperty

data class PalParkEncounter(
    @JsonProperty("area")
    val area: Area,
    @JsonProperty("base_score")
    val baseScore: Int,
    @JsonProperty("rate")
    val rate: Int
)