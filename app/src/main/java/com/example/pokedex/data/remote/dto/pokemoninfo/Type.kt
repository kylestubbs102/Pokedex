package com.example.pokedex.data.remote.dto.pokemoninfo


import com.fasterxml.jackson.annotation.JsonProperty

data class Type(
    @JsonProperty("slot")
    val slot: Int,
    @JsonProperty("type")
    val type: TypeX
)