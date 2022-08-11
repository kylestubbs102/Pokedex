package com.example.pokedex.data.remote.dto.pokemoninfo


import com.fasterxml.jackson.annotation.JsonProperty

data class GameIndice(
    @JsonProperty("game_index")
    val gameIndex: Int,
    @JsonProperty("version")
    val version: Version
)