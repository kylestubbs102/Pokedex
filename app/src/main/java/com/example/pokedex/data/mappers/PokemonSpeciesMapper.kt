package com.example.pokedex.data.mappers

import com.example.pokedex.data.local.PokemonSpeciesEntity
import com.example.pokedex.data.remote.dto.pokemonspecies.PokemonSpeciesResponse
import com.example.pokedex.util.Helpers

fun PokemonSpeciesResponse.toPokemonSpeciesEntity() = PokemonSpeciesEntity(
    id = id,
    description = getDescription(this),
    genus = getGenus(this),
    genderRate = genderRate,
    eggGroups = eggGroups.map { it.name },
    eggCycles = hatchCounter,
    evolutionChainId = evolutionChain?.url?.let {
        Helpers.getIdFromUrl(
            it
        )
    },
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
