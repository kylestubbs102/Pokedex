package com.example.pokedex.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["stat_id", "pokemon_id"])
data class BaseStatEntity(
    @ColumnInfo(name = "stat_id") val statId: Int,
    @ColumnInfo(name = "pokemon_id") val pokemonId: Int,
    val name: String,
    val value: Int,
)