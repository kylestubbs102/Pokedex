package com.example.pokedex.data.remote.dto.pokemoninfo


import com.fasterxml.jackson.annotation.JsonProperty

data class OfficialArtwork(
    @JsonProperty("front_default")
    val frontDefault: String?
)