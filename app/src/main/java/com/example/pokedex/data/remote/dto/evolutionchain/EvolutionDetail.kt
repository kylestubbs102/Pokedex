package com.example.pokedex.data.remote.dto.evolutionchain


import com.fasterxml.jackson.annotation.JsonProperty

data class EvolutionDetail(
    @JsonProperty("gender")
    val gender: Int?,
    @JsonProperty("held_item")
    val heldItem: Item?,
    @JsonProperty("item")
    val item: Any?,
    @JsonProperty("known_move")
    val knownMove: Any?,
    @JsonProperty("known_move_type")
    val knownMoveType: Any?,
    @JsonProperty("location")
    val location: Any?,
    @JsonProperty("min_affection")
    val minAffection: Int?,
    @JsonProperty("min_beauty")
    val minBeauty: Int?,
    @JsonProperty("min_happiness")
    val minHappiness: Int?,
    @JsonProperty("min_level")
    val minLevel: Int?,
    @JsonProperty("needs_overworld_rain")
    val needsOverworldRain: Boolean,
    @JsonProperty("party_species")
    val partySpecies: Any?,
    @JsonProperty("party_type")
    val partyType: Any?,
    @JsonProperty("relative_physical_stats")
    val relativePhysicalStats: Int?,
    @JsonProperty("time_of_day")
    val timeOfDay: String?,
    @JsonProperty("trade_species")
    val tradeSpecies: Any?,
    @JsonProperty("trigger")
    val trigger: Trigger?,
    @JsonProperty("turn_upside_down")
    val turnUpsideDown: Boolean
)