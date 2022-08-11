package com.example.pokedex.data.remote.dto.pokemonspecies


import com.fasterxml.jackson.annotation.JsonProperty

data class Version(
    @JsonProperty("name")
    val name: String,
    @JsonProperty("url")
    val url: String
)