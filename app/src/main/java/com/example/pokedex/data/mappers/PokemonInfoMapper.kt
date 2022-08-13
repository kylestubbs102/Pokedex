package com.example.pokedex.data.mappers

import com.example.pokedex.data.local.PokemonInfoEntity
import com.example.pokedex.data.remote.dto.pokemoninfo.PokemonInfoResponse
import com.example.pokedex.domain.model.PokemonInfo

fun PokemonInfoEntity.toPokemonInfo(): PokemonInfo =
    PokemonInfo(
        id = id,
        name = name,
        types = types,
        height = height,
        weight = weight,
        baseExperience = baseExperience,
        isLiked = isLiked,
    )

fun PokemonInfoResponse.toPokemonInfoEntity(): PokemonInfoEntity =
    PokemonInfoEntity(
        id = id,
        name = name,
        types = types.map { it.type.name },
        height = height,
        weight = weight,
        baseExperience = baseExperience,
        isLiked = false,
    )
