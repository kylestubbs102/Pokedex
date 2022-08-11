package com.example.pokedex.data.remote.dto.evolutionchain


import com.fasterxml.jackson.annotation.JsonProperty

data class EvolvesTo(
    @JsonProperty("evolution_details")
    val evolutionDetails: List<EvolutionDetail>,
    @JsonProperty("evolves_to")
    val evolvesTo: List<EvolvesTo>,
    @JsonProperty("is_baby")
    val isBaby: Boolean,
    @JsonProperty("species")
    val species: Species
)