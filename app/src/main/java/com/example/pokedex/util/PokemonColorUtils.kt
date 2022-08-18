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

    fun getPokemonIconLikedTintColor(pokemonInfo: PokemonInfo): Int {
        return when (getType(pokemonInfo)) {
            "fire" -> R.color.black
            else -> R.color.red_500
        }
    }

    private fun getType(pokemonInfo: PokemonInfo): String {
        // we just take the first type if there are two types
        return when (pokemonInfo.types.size) {
            0 -> ""
            else -> pokemonInfo.types[0]
        }
    }
}