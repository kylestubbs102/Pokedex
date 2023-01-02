package com.example.pokedex.presentation.pokemonlist

sealed class FilterViewState {
    object DefaultState : FilterViewState()
    class FilterApplied(gridState: FilterType) : FilterViewState()
    object ModalActive : FilterViewState()
}

enum class FilterType {
    FAVORITE,
    TYPES,
    GENS,
    SEARCH,
}