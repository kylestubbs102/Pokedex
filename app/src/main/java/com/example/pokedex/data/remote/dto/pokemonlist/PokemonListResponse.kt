package com.example.pokedex.data.remote.dto.pokemonlist


import com.fasterxml.jackson.annotation.JsonProperty

data class PokemonListResponse(
    @JsonProperty("count")
    val count: Int,
    @JsonProperty("next")
    val next: String?,
    @JsonProperty("previous")
    val previous: String?,
    @JsonProperty("results")
    val results: List<Result>
)