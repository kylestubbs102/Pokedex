package com.example.pokedex.data.remote.dto.pokemonspecies


import com.fasterxml.jackson.annotation.JsonProperty

data class PokedexNumber(
    @JsonProperty("entry_number")
    val entryNumber: Int,
    @JsonProperty("pokedex")
    val pokedex: Pokedex
)