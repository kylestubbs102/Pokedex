package com.example.pokedex.data.remote.dto.pokemoninfo


import com.fasterxml.jackson.annotation.JsonProperty

data class Other(
    @JsonProperty("dream_world")
    val dreamWorld: DreamWorld,
    @JsonProperty("home")
    val home: Home,
    @JsonProperty("official-artwork")
    val officialArtwork: OfficialArtwork
)