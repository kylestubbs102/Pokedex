package com.example.pokedex.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PokemonCardInfoEntity(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val name: String,
    val imageUrl: String,
    val color: String
)