package com.example.pokedex.data.remote.dto.pokemoninfo


import com.fasterxml.jackson.annotation.JsonProperty

data class Crystal(
    @JsonProperty("back_default")
    val backDefault: String?,
    @JsonProperty("back_shiny")
    val backShiny: String?,
    @JsonProperty("back_shiny_transparent")
    val backShinyTransparent: String?,
    @JsonProperty("back_transparent")
    val backTransparent: String?,
    @JsonProperty("front_default")
    val frontDefault: String?,
    @JsonProperty("front_shiny")
    val frontShiny: String?,
    @JsonProperty("front_shiny_transparent")
    val frontShinyTransparent: String?,
    @JsonProperty("front_transparent")
    val frontTransparent: String?
)