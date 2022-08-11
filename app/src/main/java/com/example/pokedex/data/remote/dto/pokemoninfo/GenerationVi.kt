package com.example.pokedex.data.remote.dto.pokemoninfo


import com.fasterxml.jackson.annotation.JsonProperty

data class GenerationVi(
    @JsonProperty("omegaruby-alphasapphire")
    val omegarubyAlphasapphire: OmegarubyAlphasapphire,
    @JsonProperty("x-y")
    val xY: XY
)