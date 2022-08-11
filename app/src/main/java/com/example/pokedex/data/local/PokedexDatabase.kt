package com.example.pokedex.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        PokemonInfoEntity::class,
        PokemonCardInfoEntity::class,
        PokemonEvolutionEntity::class,
        BaseStatEntity::class,
    ],
    version = 1
)
@TypeConverters(PokemonConverters::class)
abstract class PokedexDatabase : RoomDatabase() {
    abstract val pokedexDao: PokedexDao

    companion object {
        const val DATABASE_NAME = "pokedex_database.db"
    }
}