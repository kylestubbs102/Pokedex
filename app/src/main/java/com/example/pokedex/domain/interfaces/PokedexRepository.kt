package com.example.pokedex.domain.interfaces

import com.example.pokedex.domain.model.BaseStat
import com.example.pokedex.domain.model.PokemonAboutInfo
import com.example.pokedex.domain.model.PokemonEvolution
import com.example.pokedex.domain.model.PokemonInfo
import com.example.pokedex.util.Resource
import kotlinx.coroutines.flow.Flow

interface PokedexRepository {
    suspend fun getPokemonInfoList(
        limit: Int,
        offset: Int
    ): Flow<Resource<List<PokemonInfo>>>

    suspend fun updatePokemonIsLiked(
        id: Int,
        isLiked: Boolean
    )

    suspend fun getEvolutionChain(
        id: Int,
    ): Flow<Resource<List<PokemonEvolution>>>

    suspend fun getBaseStatsById(
        pokemonId: Int
    ): Flow<Resource<List<BaseStat>>>

    suspend fun getPokemonAboutInfoById(
        pokemonId: Int
    ): Flow<Resource<PokemonAboutInfo>>
}