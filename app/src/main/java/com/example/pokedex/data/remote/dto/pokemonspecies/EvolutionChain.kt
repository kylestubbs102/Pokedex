package com.example.pokedex.data.remote.dto.pokemonspecies


import com.fasterxml.jackson.annotation.JsonProperty

data class EvolutionChain(
    @JsonProperty("url")
    val url: String
)