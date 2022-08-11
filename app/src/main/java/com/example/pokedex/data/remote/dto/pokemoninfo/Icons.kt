package com.example.pokedex.data.remote.dto.pokemoninfo


import com.fasterxml.jackson.annotation.JsonProperty

data class Icons(
    @JsonProperty("front_default")
    val frontDefault: String?,
    @JsonProperty("front_female")
    val frontFemale: String?
)