package com.example.pokedex.util

import com.example.pokedex.R
import com.example.pokedex.domain.model.PokemonCardInfo

object PokemonColorUtils {
    fun getPokemonColor(pokemonCardInfo: PokemonCardInfo): Int {
        return when (getType(pokemonCardInfo)) {
            "bug" -> R.color.bug
            "dark" -> R.color.dark
            "dragon" -> R.color.dragon
            "electric" -> R.color.electric
            "fairy" -> R.color.fairy
            "fighting" -> R.color.fighting
            "fire" -> R.color.fire
            "flying" -> R.color.flying
            "ghost" -> R.color.ghost
            "grass" -> R.color.grass
            "ground" -> R.color.ground
            "ice" -> R.color.ice
            "normal" -> R.color.normal
            "poison" -> R.color.poison
            "psychic" -> R.color.psychic
            "rock" -> R.color.rock
            "shadow" -> R.color.shadow
            "steel" -> R.color.steel
            "water" -> R.color.water
            else -> R.color.unknown
        }
    }

    fun getPokemonTextColor(pokemonCardInfo: PokemonCardInfo): Int {
        return when (getType(pokemonCardInfo)) {
            // TODO : see all types that need to change color, also for func below
            "ice" -> R.color.black
            else -> R.color.white
        }
    }

    fun getPokemonIconLikedTintColor(pokemonCardInfo: PokemonCardInfo): Int {
        return when (getType(pokemonCardInfo)) {
            "red", "pink" -> R.color.black
            else -> R.color.red_500
        }
    }

    // also used for the not liked icon
    fun getPokemonIconTintColor(pokemonCardInfo: PokemonCardInfo): Int {
        return when (getType(pokemonCardInfo)) {
            "white", "yellow" -> R.color.black
            else -> R.color.white
        }
    }

    private fun getType(pokemonCardInfo: PokemonCardInfo): String {
        return when (pokemonCardInfo.types.size) {
            0 -> ""
            else -> pokemonCardInfo.types[0]
        }
    }
}