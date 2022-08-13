package com.example.pokedex.domain.usecases

import com.example.pokedex.domain.interfaces.PokedexRepository
import com.example.pokedex.domain.model.PokemonEvolution
import com.example.pokedex.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetEvolutionChainUseCase(
    private val pokedexRepository: PokedexRepository
) {

    operator fun invoke(pokemonId: Int): Flow<Resource<List<PokemonEvolution>>> = flow {
        pokedexRepository
            .getEvolutionChain(pokemonId)
            .collect {
                emit(it)
            }
    }
}