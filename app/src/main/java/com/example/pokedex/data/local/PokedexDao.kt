package com.example.pokedex.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PokedexDao {
    @Query("SELECT COUNT(1) FROM PokemonCardInfoEntity LIMIT :limit OFFSET :offset")
    fun checkIfPokemonExistsWithLimitAndOffset(
        limit: Int,
        offset: Int
    ): Boolean

    @Query("SELECT * FROM PokemonCardInfoEntity LIMIT :limit OFFSET :offset ")
    suspend fun selectPokemonWithLimitAndOffset(
        limit: Int,
        offset: Int
    ): List<PokemonCardInfoEntity>

    @Query("SELECT * FROM PokemonCardInfoEntity")
    suspend fun selectAllPokemon(): List<PokemonCardInfoEntity>

    @Query("SELECT * FROM PokemonInfoEntity WHERE id = :id")
    suspend fun selectPokemonWithId(id: Int): PokemonInfoEntity?

    @Query("SELECT * FROM PokemonEvolutionEntity WHERE evolution_chain_id = :evolutionChainId")
    suspend fun selectPokemonEvolutionListWithEvolutionId(evolutionChainId: Int): List<PokemonEvolutionEntity>

    @Query("SELECT * FROM BaseStatEntity WHERE pokemon_id = :pokemonId")
    fun selectBaseStatListWithPokemonId(pokemonId: Int): Flow<List<BaseStatEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPokemonCardInfoEntityList(pokemonCardInfoEntityList: List<PokemonCardInfoEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPokemonInfoEntity(pokemonInfoEntity: PokemonInfoEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPokemonEvolutionEntityList(pokeEvoEntityList: List<PokemonEvolutionEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBaseStatEntityList(baseStatEntityList: List<BaseStatEntity>)

    @Query("UPDATE PokemonInfoEntity SET is_liked = :isLiked WHERE id = :id")
    suspend fun updatePokemonIsLiked(
        id: Int,
        isLiked: Boolean
    )
}