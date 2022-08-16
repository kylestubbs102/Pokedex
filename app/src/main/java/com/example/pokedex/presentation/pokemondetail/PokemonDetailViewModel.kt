package com.example.pokedex.presentation.pokemondetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.domain.interfaces.PokedexRepository
import com.example.pokedex.domain.model.PokemonAboutInfo
import com.example.pokedex.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PokemonDetailViewModel(
    private val pokedexRepository: PokedexRepository
) : ViewModel() {

    private val _pokemonLikedFlow = MutableStateFlow<Boolean>(false)
    val pokemonLikedFlow: StateFlow<Boolean>
        get() = _pokemonLikedFlow

    private val _pokemonAboutInfoFlow =
        MutableStateFlow<Resource<PokemonAboutInfo>>(Resource.Loading())
    val pokemonAboutInfoFlow: StateFlow<Resource<PokemonAboutInfo>>
        get() = _pokemonAboutInfoFlow

    fun getPokemonAboutInfo(id: Int) {
        viewModelScope.launch {
            pokedexRepository
                .getPokemonAboutInfoById(id)
                .collect {
                    _pokemonAboutInfoFlow.emit(it)
                }
        }
    }

    fun updateLikedValue(
        id: Int,
        isLiked: Boolean
    ) {
        viewModelScope.launch {
            pokedexRepository
                .updatePokemonIsLiked(
                    id,
                    isLiked
                )
        }
    }

    fun getPokemonLikedValue(id: Int) {
        viewModelScope.launch {
            pokedexRepository
                .getPokemonIsLiked(id)
                .collect {
                    _pokemonLikedFlow.emit(it)
                }
        }
    }
}