package com.example.pokedex.data.remote.dto.pokemonspecies


import com.fasterxml.jackson.annotation.JsonProperty

data class FlavorTextEntry(
    @JsonProperty("flavor_text")
    val flavorText: String,
    @JsonProperty("language")
    val language: Language,
    @JsonProperty("version")
    val version: Version
)