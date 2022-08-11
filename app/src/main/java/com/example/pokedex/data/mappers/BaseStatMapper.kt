package com.example.pokedex.data.mappers

import com.example.pokedex.data.local.BaseStatEntity
import com.example.pokedex.data.remote.dto.pokemoninfo.PokemonInfoResponse
import com.example.pokedex.domain.model.BaseStat
import com.example.pokedex.util.Helpers

fun PokemonInfoResponse.toBaseStatEntityList(): List<BaseStatEntity> =
    stats.map {
        BaseStatEntity(
            statId = Helpers.getIdFromUrl(it.stat.url),
            pokemonId = id,
            value = it.baseStat,
            name = parseName(it.stat.name),

            )
    }

fun BaseStatEntity.toBaseStat(): BaseStat =
    BaseStat(
        statId = statId,
        pokemonId = pokemonId,
        name = name,
        value = value,
    )

private fun parseName(name: String) =
    name.replace("-", ". ")
        .replace("special", "sp")
        .replace("attack", "atk")
        .replace("defense", "def")
        .split(" ")
        .joinToString(" ") {
            it.replaceFirstChar { c -> c.uppercase() }
        }