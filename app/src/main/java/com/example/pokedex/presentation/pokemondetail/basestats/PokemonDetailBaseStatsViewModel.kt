package com.example.pokedex.presentation.pokemondetail.basestats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.domain.model.BaseStat
import com.example.pokedex.domain.usecases.GetBaseStatsUseCase
import com.example.pokedex.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokemonDetailBaseStatsViewModel(
    private val getBaseStatsUseCase: GetBaseStatsUseCase
) : ViewModel() {

    private val _baseStatsStateFlow = MutableStateFlow<Resource<List<BaseStat>>>(Resource.Loading())
    val baseStatsStateFlow: StateFlow<Resource<List<BaseStat>>>
        get() = _baseStatsStateFlow

    fun getBaseStats(pokemonId: Int) {
        viewModelScope.launch {
            getBaseStatsUseCase(pokemonId)
                .collect {
                    _baseStatsStateFlow.emit(it)
                }
        }
    }
}