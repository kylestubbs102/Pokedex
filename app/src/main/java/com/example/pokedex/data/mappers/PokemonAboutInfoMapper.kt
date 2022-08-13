package com.example.pokedex.data.mappers

import com.example.pokedex.data.local.PokemonInfoEntity
import com.example.pokedex.data.local.PokemonSpeciesEntity
import com.example.pokedex.domain.model.PokemonAboutInfo

object PokemonAboutInfoMapper {
    fun toPokemonAboutInfo(
        pokemonInfoEntity: PokemonInfoEntity,
        pokemonSpeciesEntity: PokemonSpeciesEntity,
    ): PokemonAboutInfo = PokemonAboutInfo(
        genus = pokemonSpeciesEntity.genus,
        description = pokemonSpeciesEntity.description,
        height = pokemonInfoEntity.height,
        weight = pokemonInfoEntity.weight,
        genderRate = pokemonSpeciesEntity.genderRate,
        eggGroups = pokemonSpeciesEntity.eggGroups,
        eggCycles = pokemonSpeciesEntity.eggCycles,
        baseExperience = pokemonInfoEntity.baseExperience,
    )
}