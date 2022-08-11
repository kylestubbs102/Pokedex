package com.example.pokedex.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["id", "evolved_species_id", "evolution_chain_id"])
data class PokemonEvolutionEntity(
    @ColumnInfo(name = "evolution_chain_id") val evolutionChainId: Int,
    val id: Int,
    @ColumnInfo(name = "evolved_species_id") val evolvedSpeciesId: Int,
    val name: String,
    @ColumnInfo(name = "evolved_species_name") val evolvedSpeciesName: String,
    @ColumnInfo(name = "min_level") val minLevel: Int?,
)
