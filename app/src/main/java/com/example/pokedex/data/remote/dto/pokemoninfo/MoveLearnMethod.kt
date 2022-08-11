package com.example.pokedex.data.remote.dto.pokemoninfo


import com.fasterxml.jackson.annotation.JsonProperty

data class MoveLearnMethod(
    @JsonProperty("name")
    val name: String,
    @JsonProperty("url")
    val url: String
)