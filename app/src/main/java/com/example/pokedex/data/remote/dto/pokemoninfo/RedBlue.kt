package com.example.pokedex.data.remote.dto.pokemoninfo


import com.fasterxml.jackson.annotation.JsonProperty

data class RedBlue(
    @JsonProperty("back_default")
    val backDefault: String?,
    @JsonProperty("back_gray")
    val backGray: String?,
    @JsonProperty("back_transparent")
    val backTransparent: String?,
    @JsonProperty("front_default")
    val frontDefault: String?,
    @JsonProperty("front_gray")
    val frontGray: String?,
    @JsonProperty("front_transparent")
    val frontTransparent: String?
)