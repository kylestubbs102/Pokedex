package com.example.pokedex.data.local

import androidx.room.TypeConverter
import com.example.pokedex.data.remote.dto.pokemoninfo.Stat
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class PokemonConverters {
    @TypeConverter
    fun stringToStringList(string: String): List<String> {
        return Json.decodeFromString(string)
    }

    @TypeConverter
    fun stringListToString(stringList: List<String>): String {
        return Json.encodeToString(stringList)
    }
}