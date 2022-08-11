package com.example.pokedex.util

import com.example.pokedex.R

object PokemonColorUtils {
    fun getPokemonColor(color: String): Int {
        return when (color) {
            "black" -> R.color.black
            "blue" -> R.color.blue_500
            "brown" -> R.color.brown_500
            "gray" -> R.color.gray_500
            "green" -> R.color.green_500
            "pink" -> R.color.pink_500
            "purple" -> R.color.purple_500
            "red" -> R.color.red_500
            "white" -> R.color.white
            "yellow" -> R.color.yellow_500
            else -> R.color.gray_500
        }
    }

    fun getPokemonTextColor(color: String): Int {
        return when (color) {
            "white", "yellow" -> R.color.black
            else -> R.color.white
        }
    }

    fun getPokemonIconLikedTintColor(color: String): Int {
        return when (color) {
            "red", "pink" -> R.color.black
            else -> R.color.red_500
        }
    }

    // also used for the not liked icon
    fun getPokemonIconTintColor(color: String): Int {
        return when (color) {
            "white", "yellow" -> R.color.black
            else -> R.color.white
        }
    }
}