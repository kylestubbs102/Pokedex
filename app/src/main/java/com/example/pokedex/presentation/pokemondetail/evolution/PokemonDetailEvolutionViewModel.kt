package com.example.pokedex.presentation.pokemondetail.evolution

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.domain.model.PokemonEvolution
import com.example.pokedex.domain.usecases.GetEvolutionChainUseCase
import com.example.pokedex.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokemonDetailEvolutionViewModel(
    private val getEvolutionChainUseCase: GetEvolutionChainUseCase
) : ViewModel() {

    private val _pokemonEvolutionFlow =
        MutableStateFlow<Resource<List<PokemonEvolution>>>(Resource.Loading())
    val pokemonEvolutionFlow: StateFlow<Resource<List<PokemonEvolution>>>
        get() = _pokemonEvolutionFlow

    fun fetchPokemonEvolutionList(id: Int) {
        viewModelScope.launch {
            getEvolutionChainUseCase(id)
                .collect {
                    _pokemonEvolutionFlow.emit(it)
                }
        }
    }
}