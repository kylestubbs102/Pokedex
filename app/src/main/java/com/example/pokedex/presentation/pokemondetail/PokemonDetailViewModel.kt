package com.example.pokedex.presentation.pokemondetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.domain.model.PokemonEvolution
import com.example.pokedex.domain.model.PokemonInfo
import com.example.pokedex.domain.repository.PokedexRepository
import com.example.pokedex.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PokemonDetailViewModel(
    private val pokedexRepository: PokedexRepository
) : ViewModel() {

    private val _pokemonEvolutionFlow = MutableStateFlow<Resource<List<PokemonEvolution>>>(Resource.Loading())
    val pokemonEvolutionFlow: StateFlow<Resource<List<PokemonEvolution>>>
        get() = _pokemonEvolutionFlow

    private val _pokemonInfoFlow = MutableStateFlow<Resource<PokemonInfo>>(Resource.Loading())
    val pokemonInfoFlow: StateFlow<Resource<PokemonInfo>>
        get() = _pokemonInfoFlow

    fun fetchPokemonInfo(id: Int) {
        viewModelScope.launch {
            pokedexRepository
                .getPokemonInfoById(id)
                .collect {
                    _pokemonInfoFlow.emit(it)
                }
        }
    }

    fun updateLikedValue(
        id: Int,
        isLiked: Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            pokedexRepository
                .updatePokemonIsLiked(
                    id,
                    isLiked
                )
        }
    }

    fun fetchPokemonEvolutionList(
        id: Int,
        evolutionChainId: Int
    ) {
        viewModelScope.launch {
            pokedexRepository
                .fetchEvolutionChain(id, evolutionChainId)
                .collect {
                    _pokemonEvolutionFlow.emit(it)
                }
        }
    }

}