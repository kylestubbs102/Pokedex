package com.example.pokedex.domain.usecases

import com.example.pokedex.domain.interfaces.PokedexRepository
import com.example.pokedex.util.Resource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

class GetPokemonAboutInfoUseCase(
    private val pokedexRepository: PokedexRepository
) {
    operator fun invoke(
        pokemonId: Int
    ) = flow {
        pokedexRepository
            .getPokemonAboutInfoById(pokemonId)
            .collect {
                emit(it)
            }
    }
}