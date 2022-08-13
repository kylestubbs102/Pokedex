package com.example.pokedex.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PokemonInfoEntity(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val name: String,
    val types: List<String>,
    val height: Int,    // in deciliters
    val weight: Int,    // in hectograms
    @ColumnInfo(name = "base_experience") val baseExperience: Int,
    @ColumnInfo(name = "is_liked") val isLiked: Boolean,
)
