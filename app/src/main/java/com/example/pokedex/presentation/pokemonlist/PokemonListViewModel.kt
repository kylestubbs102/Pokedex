package com.example.pokedex.presentation.pokemonlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.local.PokedexDao
import com.example.pokedex.data.mappers.toPokemonInfo
import com.example.pokedex.domain.interfaces.PokedexRepository
import com.example.pokedex.domain.model.PokemonInfo
import com.example.pokedex.util.Constants.POKEMON_API_LIMIT
import com.example.pokedex.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokemonListViewModel(
    private val pokedexRepository: PokedexRepository,
    private val pokedexDao: PokedexDao
) : ViewModel() {

    private val _pokemonInfoListState = MutableStateFlow(PokemonListState())
    val pokemonInfoListState: StateFlow<PokemonListState>
        get() = _pokemonInfoListState

    // TODO : refactor to include enum class for all fab types (ex: when search is clicked don't disable modal)
    private val _filterViewState = MutableLiveData<FilterViewState>(FilterViewState.DefaultState)
    val filterViewState: LiveData<FilterViewState>
        get() = _filterViewState

    private var currentFetchJob: Job? = null

    // init has to be after _pokemonInfoListState in order to avoid NPE when fetch list call is made
    init {
        fetchPokemonList(offset = 0)
    }

    fun updateFilterState(filterViewState: FilterViewState) {
        _filterViewState.postValue(filterViewState)
    }

    fun fetchPokemonList(offset: Int = 0) {
        currentFetchJob = viewModelScope.launch {
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

    fun favoriteButtonClicked(showFavorites: Boolean) {
        currentFetchJob?.cancel()
        currentFetchJob = viewModelScope.launch(Dispatchers.IO) {
            // need to use dao in case pokemon were liked, so we need to retrieve the most recent
            // data, maybe refactor to repository

            // reset to original list
            if (showFavorites.not()) {
                // leaving this here in case needed, this will reset to top of page, might be necessary
//                _pokemonInfoListState.value = PokemonListState()
//                fetchPokemonList(0)
                val allPokemon = pokedexDao.selectAllPokemon()
                    .map { it.toPokemonInfo() }
                _pokemonInfoListState.value = _pokemonInfoListState.value.copy(
                    pokemonList = allPokemon
                )
                return@launch
            }

            val likedPokemon = pokedexDao.selectAllPokemon()
                .filter { it.isLiked }
                .map { it.toPokemonInfo() }
            _pokemonInfoListState.let {
                it.emit(
                    it.value.copy(
                        pokemonList = likedPokemon
                    )
                )
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