package com.example.pokedex.data.remote.dto.evolutionchain


import com.fasterxml.jackson.annotation.JsonProperty

data class Species(
    @JsonProperty("name")
    val name: String,
    @JsonProperty("url")
    val url: String
)