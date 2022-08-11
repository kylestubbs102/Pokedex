package com.example.pokedex.data.remote.dto.pokemonlist


import com.fasterxml.jackson.annotation.JsonProperty

data class Result(
    @JsonProperty("name")
    val name: String,
    @JsonProperty("url")
    val url: String
)