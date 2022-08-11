package com.example.pokedex.domain.interfaces

import com.example.pokedex.data.local.BaseStatEntity
import com.example.pokedex.domain.model.BaseStat
import com.example.pokedex.domain.model.PokemonCardInfo
import com.example.pokedex.domain.model.PokemonEvolution
import com.example.pokedex.domain.model.PokemonInfo
import com.example.pokedex.util.Resource
import kotlinx.coroutines.flow.Flow

interface PokedexRepository {
    suspend fun getPokemonCardInfoList(
        limit: Int,
        offset: Int,
        isInitialFetch: Boolean
    ): Flow<Resource<List<PokemonCardInfo>>>

    suspend fun getPokemonInfoById(
        id: Int
    ): Flow<Resource<PokemonInfo>>

    suspend fun updatePokemonIsLiked(
        id: Int,
        isLiked: Boolean
    )

    suspend fun fetchEvolutionChain(
        id: Int,
        evolutionChainId: Int
    ): Flow<Resource<List<PokemonEvolution>>>

    suspend fun fetchBaseStatsById(
        pokemonId: Int
    ): Flow<Resource<List<BaseStat>>>
}