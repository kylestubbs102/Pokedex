package com.example.pokedex.data.remote.dto.pokemoninfo


import com.fasterxml.jackson.annotation.JsonProperty

data class GenerationI(
    @JsonProperty("red-blue")
    val redBlue: RedBlue,
    @JsonProperty("yellow")
    val yellow: Yellow
)