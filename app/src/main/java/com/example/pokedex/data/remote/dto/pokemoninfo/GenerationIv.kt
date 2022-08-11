package com.example.pokedex.data.remote.dto.pokemoninfo


import com.fasterxml.jackson.annotation.JsonProperty

data class GenerationIv(
    @JsonProperty("diamond-pearl")
    val diamondPearl: DiamondPearl,
    @JsonProperty("heartgold-soulsilver")
    val heartgoldSoulsilver: HeartgoldSoulsilver,
    @JsonProperty("platinum")
    val platinum: Platinum
)