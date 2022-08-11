package com.example.pokedex.data.remote.dto.pokemoninfo


import com.fasterxml.jackson.annotation.JsonProperty

data class GenerationIi(
    @JsonProperty("crystal")
    val crystal: Crystal,
    @JsonProperty("gold")
    val gold: Gold,
    @JsonProperty("silver")
    val silver: Silver
)