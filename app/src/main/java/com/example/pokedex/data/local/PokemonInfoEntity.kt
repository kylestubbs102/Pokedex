package com.example.pokedex.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.pokedex.data.remote.dto.pokemoninfo.Stat

@Entity
data class PokemonInfoEntity(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val name: String,
    val description: String,
    val category: String,
    val types: List<String>,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    val color: String,
    @ColumnInfo(name = "is_liked") val isLiked: Boolean,
    val height: Int,    // in deciliters
    val weight: Int,    // in hectograms
    @ColumnInfo(name = "gender_rate") val genderRate: Int,   // in 8ths of female %, ex: 1 = 1/8 female
    @ColumnInfo(name = "egg_groups") val eggGroups: List<String>,
    @ColumnInfo(name = "egg_cycles") val eggCycles: Int,
    @ColumnInfo(name = "base_experience") val baseExperience: Int,
    @ColumnInfo(name = "evolution_chain_id") val evolutionChainId: Int?,
)
