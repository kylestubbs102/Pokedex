package com.example.pokedex.data.remote.dto.pokemoninfo


import com.fasterxml.jackson.annotation.JsonProperty

data class Emerald(
    @JsonProperty("front_default")
    val frontDefault: String?,
    @JsonProperty("front_shiny")
    val frontShiny: String?
)