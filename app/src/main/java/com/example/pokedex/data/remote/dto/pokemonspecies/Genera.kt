package com.example.pokedex.data.remote.dto.pokemonspecies


import com.fasterxml.jackson.annotation.JsonProperty

data class Genera(
    @JsonProperty("genus")
    val genus: String,
    @JsonProperty("language")
    val language: LanguageX
)