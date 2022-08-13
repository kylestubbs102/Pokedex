package com.example.pokedex.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PokemonSpeciesEntity(
    @PrimaryKey val id: Int,
    val description: String,
    val genus: String,
    @ColumnInfo(name = "gender_rate") val genderRate: Int,   // in 8ths of female %, ex: 1 = 1/8 female
    @ColumnInfo(name = "egg_groups") val eggGroups: List<String>,
    @ColumnInfo(name = "egg_cycles") val eggCycles: Int,
    @ColumnInfo(name = "evolution_chain_id") val evolutionChainId: Int?,
)