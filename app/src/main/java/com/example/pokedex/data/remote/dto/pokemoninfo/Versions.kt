package com.example.pokedex.data.remote.dto.pokemoninfo


import com.fasterxml.jackson.annotation.JsonProperty

data class Versions(
    @JsonProperty("generation-i")
    val generationI: GenerationI,
    @JsonProperty("generation-ii")
    val generationIi: GenerationIi,
    @JsonProperty("generation-iii")
    val generationIii: GenerationIii,
    @JsonProperty("generation-iv")
    val generationIv: GenerationIv,
    @JsonProperty("generation-v")
    val generationV: GenerationV,
    @JsonProperty("generation-vi")
    val generationVi: GenerationVi,
    @JsonProperty("generation-vii")
    val generationVii: GenerationVii,
    @JsonProperty("generation-viii")
    val generationViii: GenerationViii
)