package com.example.pokedex.domain.usecases

import com.example.pokedex.domain.model.BaseStat
import com.example.pokedex.domain.interfaces.PokedexRepository
import com.example.pokedex.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetBaseStatsUseCase(
    private val pokedexRepository: PokedexRepository
) {
    operator fun invoke(
        pokemonId: Int
    ): Flow<Resource<List<BaseStat>>> = flow {
        pokedexRepository
            .fetchBaseStatsById(pokemonId)
            .collect {
                emit(it)
            }
    }
}