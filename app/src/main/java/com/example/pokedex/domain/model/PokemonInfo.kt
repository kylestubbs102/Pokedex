package com.example.pokedex.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonInfo(
    val id: Int,
    val name: String,
    val types: List<String>,
    val height: Int,    // deciliters
    val weight: Int,    // hectograms
    val baseExperience: Int,
    val isLiked: Boolean,
) : Parcelable
