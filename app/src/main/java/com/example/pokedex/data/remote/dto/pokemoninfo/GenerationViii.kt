package com.example.pokedex.data.remote.dto.pokemoninfo


import com.fasterxml.jackson.annotation.JsonProperty

data class GenerationViii(
    @JsonProperty("icons")
    val icons: IconsX
)