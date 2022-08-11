package com.example.pokedex.presentation.pokemoncardlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.domain.model.PokemonCardInfo
import com.example.pokedex.domain.interfaces.PokedexRepository
import com.example.pokedex.util.Constants.POKEMON_API_LIMIT
import com.example.pokedex.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokemonCardListViewModel(
    private val pokedexRepository: PokedexRepository
) : ViewModel() {

    init {
        fetchPokemonList(
            offset = 0,
            isInitialFetch = true,
        )
    }

    private val _pokemonCardInfoListState = MutableStateFlow(PokemonCardListState())
    val pokemonCardInfoListState: StateFlow<PokemonCardListState>
        get() = _pokemonCardInfoListState

    fun fetchPokemonList(
        offset: Int,
        isInitialFetch: Boolean
    ) {
        viewModelScope.launch {
            pokedexRepository
                .getPokemonCardInfoList(
                    limit = POKEMON_API_LIMIT,
                    offset = offset,
                    isInitialFetch = isInitialFetch
                )
                .collect {
                    checkAndUpdateState(it)
                }
        }
    }

    private suspend fun checkAndUpdateState(
        resource: Resource<List<PokemonCardInfo>>
    ) {
        when (resource) {
            is Resource.Loading -> {
                _pokemonCardInfoListState.emit(
                    PokemonCardListState(
                        isLoading = true,
                        pokemonCardList = _pokemonCardInfoListState.value.pokemonCardList,
                    )
                )
            }
            is Resource.Success -> {
                val newListCards = resource.data ?: emptyList()
                val newList = _pokemonCardInfoListState.value.pokemonCardList + newListCards
                _pokemonCardInfoListState.emit(
                    PokemonCardListState(
                        pokemonCardList = newList
                    )
                )
            }
            is Resource.Error -> {
                _pokemonCardInfoListState.emit(
                    PokemonCardListState(
                        pokemonCardList = _pokemonCardInfoListState.value.pokemonCardList,
                        errorMessage = resource.message ?: "Error occurred."
                    )
                )
            }
        }
    }

}