package com.example.pokedex.data.remote.dto.pokemoninfo


import com.fasterxml.jackson.annotation.JsonProperty

data class GenerationVii(
    @JsonProperty("icons")
    val icons: Icons,
    @JsonProperty("ultra-sun-ultra-moon")
    val ultraSunUltraMoon: UltraSunUltraMoon
)