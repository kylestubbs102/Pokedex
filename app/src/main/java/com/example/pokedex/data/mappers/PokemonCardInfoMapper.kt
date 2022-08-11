package com.example.pokedex.data.mappers

import com.example.pokedex.data.local.PokemonCardInfoEntity
import com.example.pokedex.domain.model.PokemonCardInfo

fun PokemonCardInfoEntity.toPokemonCardInfo(): PokemonCardInfo =
    PokemonCardInfo(
        id = id,
        name = name,
        imageUrl = imageUrl,
        color = color,
    )