package com.example.pokedex.data.remote.dto.evolutionchain


import com.fasterxml.jackson.annotation.JsonProperty

data class EvolutionChainResponse(
    @JsonProperty("baby_trigger_item")
    val babyTriggerItem: Any?,
    @JsonProperty("chain")
    val chain: EvolvesTo,
    @JsonProperty("id")
    val id: Int
)