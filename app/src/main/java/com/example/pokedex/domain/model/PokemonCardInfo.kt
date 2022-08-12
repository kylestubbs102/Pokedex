package com.example.pokedex.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonCardInfo(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<String>,
) : Parcelable
