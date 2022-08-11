package com.example.pokedex.data.remote.dto.pokemonspecies


import com.fasterxml.jackson.annotation.JsonProperty

data class Variety(
    @JsonProperty("is_default")
    val isDefault: Boolean,
    @JsonProperty("pokemon")
    val pokemon: Pokemon
)