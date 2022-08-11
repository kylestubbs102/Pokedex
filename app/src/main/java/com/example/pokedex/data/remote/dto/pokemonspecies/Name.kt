package com.example.pokedex.data.remote.dto.pokemonspecies


import com.fasterxml.jackson.annotation.JsonProperty

data class Name(
    @JsonProperty("language")
    val language: LanguageXX,
    @JsonProperty("name")
    val name: String
)