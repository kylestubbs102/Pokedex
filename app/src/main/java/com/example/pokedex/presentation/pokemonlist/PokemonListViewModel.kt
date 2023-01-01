package com.example.pokedex.presentation.pokemonlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.domain.interfaces.PokedexRepository
import com.example.pokedex.domain.model.PokemonInfo
import com.example.pokedex.util.Constants.POKEMON_API_LIMIT
import com.example.pokedex.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokemonListViewModel(
    private val pokedexRepository: PokedexRepository
) : ViewModel() {

    init {
        fetchPokemonList(offset = 0)
    }

    private val _pokemonInfoListState = MutableStateFlow(PokemonListState())
    val pokemonInfoListState: StateFlow<PokemonListState>
        get() = _pokemonInfoListState

    // TODO : refactor to include enum class for all fab types (ex: when search is clicked don't disable modal)
    private val _modalIsActive = MutableLiveData(false)
    val modalIsActive: LiveData<Boolean>
        get() = _modalIsActive

    fun updateModalState(isActive: Boolean) {
        _modalIsActive.postValue(isActive)
    }

    fun fetchPokemonList(offset: Int) {
        viewModelScope.launch {
            pokedexRepository
                .getPokemonInfoList(
                    limit = POKEMON_API_LIMIT,
                    offset = offset,
                )
                .collect {
                    checkAndUpdateState(it)
                }
        }
    }

    private suspend fun checkAndUpdateState(
        resource: Resource<List<PokemonInfo>>
    ) {
        when (resource) {
            is Resource.Loading -> {
                _pokemonInfoListState.emit(
                    PokemonListState(
                        isLoading = true,
                        pokemonList = _pokemonInfoListState.value.pokemonList,
                    )
                )
            }
            is Resource.Success -> {
                val newValues = resource.data ?: emptyList()
                val newList = _pokemonInfoListState.value.pokemonList + newValues
                _pokemonInfoListState.emit(
                    PokemonListState(
                        pokemonList = newList
                    )
                )
            }
            is Resource.Error -> {
                _pokemonInfoListState.emit(
                    PokemonListState(
                        pokemonList = _pokemonInfoListState.value.pokemonList,
                        errorMessage = resource.message ?: "Error occurred."
                    )
                )
            }
        }
    }

}