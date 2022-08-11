package com.example.pokedex.data.remote.dto.pokemoninfo


import com.fasterxml.jackson.annotation.JsonProperty

data class Home(
    @JsonProperty("front_default")
    val frontDefault: String?,
    @JsonProperty("front_female")
    val frontFemale: String?,
    @JsonProperty("front_shiny")
    val frontShiny: String?,
    @JsonProperty("front_shiny_female")
    val frontShinyFemale: String?
)