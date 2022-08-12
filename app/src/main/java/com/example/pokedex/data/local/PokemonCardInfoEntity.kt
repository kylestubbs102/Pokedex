package com.example.pokedex.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PokemonCardInfoEntity(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val name: String,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    val types: List<String>,
)