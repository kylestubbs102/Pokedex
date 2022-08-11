package com.example.pokedex.util

object Helpers {
    fun getIdFromUrl(url: String): Int {
        return url
            .filter { it.isDigit() }    // only leave digits in url
            .removeRange(0, 1)          // remove '2' in 'api/v2'
            .toInt()
    }

    fun getImageUrl(id: Int): String {
        return when (id <= 898 || (id in 10001..10180)) {
            true -> "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
            false -> "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
        }
    }
}