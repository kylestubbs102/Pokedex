package com.example.pokedex.data.mappers

import com.example.pokedex.data.local.PokemonInfoEntity
import com.example.pokedex.data.remote.dto.pokemoninfo.PokemonInfoResponse
import com.example.pokedex.data.remote.dto.pokemonspecies.PokemonSpeciesResponse
import com.example.pokedex.domain.model.PokemonInfo
import com.example.pokedex.util.Helpers

fun PokemonInfoEntity.toPokemonInfo(): PokemonInfo =
    PokemonInfo(
        id = id,
        name = name,
        description = description,
        genus = genus,
        types = types,
        imageUrl = imageUrl,
        color = color,
        isLiked = isLiked,
        height = height,
        weight = weight,
        genderRate = genderRate,
        eggGroups = eggGroups,
        eggCycles = eggCycles,
        baseExperience = baseExperience,
        evolutionChainId = evolutionChainId,
    )

fun PokemonInfoResponse.toPokemonInfoEntity(
    pokemonSpeciesResponse: PokemonSpeciesResponse,
): PokemonInfoEntity =
    PokemonInfoEntity(
        id = id,
        name = name,
        description = getDescription(pokemonSpeciesResponse),
        genus = getGenus(pokemonSpeciesResponse),
        types = types.map { it.type.name },
        imageUrl = Helpers.getImageUrl(id),
        color = pokemonSpeciesResponse.color.name,
        isLiked = false,
        height = height,
        weight = weight,
        genderRate = pokemonSpeciesResponse.genderRate,
        eggGroups = pokemonSpeciesResponse.eggGroups.map { it.name },
        eggCycles = pokemonSpeciesResponse.hatchCounter,
        baseExperience = baseExperience,
        evolutionChainId = pokemonSpeciesResponse.evolutionChain?.url?.let { Helpers.getIdFromUrl(it) },
    )

private fun getGenus(pokemonSpeciesResponse: PokemonSpeciesResponse): String {
    return pokemonSpeciesResponse
        .genera
        .lastOrNull { it.language.name == "en" }
        ?.genus
        ?: ""
}

private fun getDescription(pokemonSpeciesResponse: PokemonSpeciesResponse): String {
    val str = pokemonSpeciesResponse
        .flavorTextEntries
        .firstOrNull {
            it.language.name == "en" && it.version.name == "ruby"
        }
        ?.flavorText
        ?.replace("\n", " ")
        ?.replace("\u000C", " ")    // "\f"
        ?: "Description unavailable."

    val lowercaseSentences = str
        .lowercase()
        .split(".")
        .filter { it.isNotEmpty() }

    val uppercaseSentences = lowercaseSentences
        .map { sentence ->
            sentence
                .trim()
                .replaceFirstChar {
                    it.uppercase()
                }
        }

    return uppercaseSentences.joinToString(". ")
        .trim()
        .plus(".")
}
