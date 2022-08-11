package com.example.pokedex.data.mappers

import com.example.pokedex.data.local.PokemonEvolutionEntity
import com.example.pokedex.data.remote.dto.evolutionchain.EvolutionChainResponse
import com.example.pokedex.data.remote.dto.evolutionchain.EvolvesTo
import com.example.pokedex.domain.model.PokemonEvolution
import com.example.pokedex.util.Helpers
import java.util.LinkedList
import java.util.Queue

fun PokemonEvolutionEntity.toPokemonEvolution(): PokemonEvolution =
    PokemonEvolution(
        evolutionChainId = evolutionChainId,
        id = id,
        evolvedSpeciesId = evolvedSpeciesId,
        name = name,
        evolvedSpeciesName = evolvedSpeciesName,
        minLevel = minLevel,
    )

fun EvolutionChainResponse.toPokemonEvolutionEntityList(): List<PokemonEvolutionEntity> {

    val pokemonEvolutionList = mutableListOf<PokemonEvolutionEntity>()

    // bfs through tree to create list of PokemonEvolution
    val queue: Queue<EvolvesTo> = LinkedList()
    val chainStart = chain
    queue.add(chainStart)
    while (queue.isNotEmpty()) {
        val size = queue.size
        val element = queue.element()
        for (i in 1..size) {
            element.evolvesTo.forEach {
                val pokemonEvolution = toPokemonEvolution(id, element, it)
                pokemonEvolutionList.add(pokemonEvolution)

                if (it.evolvesTo.isEmpty()) {
                    return@forEach
                }

                queue.add(it)
            }
            queue.remove()
        }
    }

    return pokemonEvolutionList
}

private fun toPokemonEvolution(
    evolutionChainId: Int,
    currentPokemon: EvolvesTo,
    evolvedPokemon: EvolvesTo
): PokemonEvolutionEntity {
    return PokemonEvolutionEntity(
        evolutionChainId = evolutionChainId,
        id = Helpers.getIdFromUrl(currentPokemon.species.url),
        evolvedSpeciesId = Helpers.getIdFromUrl(evolvedPokemon.species.url),
        name = currentPokemon.species.name.replaceFirstChar { c -> c.uppercase() },
        evolvedSpeciesName = evolvedPokemon.species.name.replaceFirstChar { c -> c.uppercase() },
        minLevel = evolvedPokemon.evolutionDetails
            .filter { it.minLevel != null }
            .maxByOrNull { it.minLevel as Int }
            ?.minLevel,
    )
}