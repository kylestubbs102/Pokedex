package com.example.pokedex.data.remote.dto.pokemoninfo


import com.fasterxml.jackson.annotation.JsonProperty

data class GenerationV(
    @JsonProperty("black-white")
    val blackWhite: BlackWhite
)