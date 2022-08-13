package com.example.pokedex.util

import com.example.pokedex.R
import com.example.pokedex.domain.model.PokemonInfo

object PokemonColorUtils {
    fun getPokemonColor(pokemonInfo: PokemonInfo): Int {
        return when (getType(pokemonInfo)) {
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

    fun getPokemonTextColor(pokemonInfo: PokemonInfo): Int {
        return when (getType(pokemonInfo)) {
            // TODO : see all types that need to change color, also for func below
            "ice" -> R.color.black
            else -> R.color.white
        }
    }

    fun getPokemonIconLikedTintColor(pokemonInfo: PokemonInfo): Int {
        return when (getType(pokemonInfo)) {
            "red", "pink" -> R.color.black
            else -> R.color.red_500
        }
    }

    // also used for the not liked icon
    fun getPokemonIconTintColor(pokemonInfo: PokemonInfo): Int {
        return when (getType(pokemonInfo)) {
            "white", "yellow" -> R.color.black
            else -> R.color.white
        }
    }

    private fun getType(pokemonInfo: PokemonInfo): String {
        return when (pokemonInfo.types.size) {
            0 -> ""
            else -> pokemonInfo.types[0]
        }
    }
}