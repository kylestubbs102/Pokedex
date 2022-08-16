package com.example.pokedex.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PokedexDao {
    @Query("SELECT COUNT(1) FROM PokemonInfoEntity LIMIT :limit OFFSET :offset")
    fun checkIfPokemonExistsWithLimitAndOffset(limit: Int, offset: Int): Boolean

    @Query("SELECT * FROM PokemonInfoEntity LIMIT :limit OFFSET :offset ")
    suspend fun selectPokemonWithLimitAndOffset(limit: Int, offset: Int): List<PokemonInfoEntity>

    @Query("SELECT * FROM PokemonInfoEntity")
    suspend fun selectAllPokemon(): List<PokemonInfoEntity>

    @Query("SELECT * FROM PokemonInfoEntity WHERE id = :id")
    suspend fun selectPokemonInfoWithId(id: Int): PokemonInfoEntity?

    @Query("SELECT is_liked FROM PokemonInfoEntity WHERE id = :id")
    suspend fun selectPokemonIsLikedWithId(id: Int): Boolean?

    @Query("UPDATE PokemonInfoEntity SET is_liked = :isLiked WHERE id = :id")
    suspend fun updatePokemonIsLiked(id: Int, isLiked: Boolean)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPokemonInfoEntityList(pokemonInfoEntityList: List<PokemonInfoEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPokemonInfoEntity(pokemonInfoEntity: PokemonInfoEntity)


    @Query("SELECT * FROM PokemonSpeciesEntity WHERE id = :id")
    suspend fun selectPokemonSpeciesWithId(id: Int): PokemonSpeciesEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPokemonSpeciesEntity(pokemonSpeciesEntity: PokemonSpeciesEntity)


    @Query("SELECT evolution_chain_id FROM PokemonSpeciesEntity WHERE id = :id")
    fun selectEvolutionChainIdWithId(id: Int): Flow<Int?>

    @Query("SELECT * FROM PokemonEvolutionEntity WHERE evolution_chain_id = :evolutionChainId")
    suspend fun selectPokemonEvolutionListWithEvolutionId(evolutionChainId: Int): List<PokemonEvolutionEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPokemonEvolutionEntityList(pokeEvoEntityList: List<PokemonEvolutionEntity>)


    @Query("SELECT * FROM BaseStatEntity WHERE pokemon_id = :pokemonId")
    fun selectBaseStatListWithPokemonId(pokemonId: Int): Flow<List<BaseStatEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBaseStatEntityList(baseStatEntityList: List<BaseStatEntity>)
}