package com.example.pokedex.data.remote

import com.example.pokedex.data.remote.dto.evolutionchain.EvolutionChainResponse
import com.example.pokedex.data.remote.dto.pokemoninfo.PokemonInfoResponse
import com.example.pokedex.data.remote.dto.pokemonlist.PokemonListResponse
import com.example.pokedex.data.remote.dto.pokemonspecies.PokemonSpeciesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokedexApi {
    @GET("pokemon")
    suspend fun getPokemonListByLimitAndOffset(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): PokemonListResponse

    @GET("pokemon/{pokemonId}")
    suspend fun getPokemonInfoById(
        @Path("pokemonId") pokemonId: Int
    ): PokemonInfoResponse

    @GET("pokemon-species/{pokemonId}")
    suspend fun getPokemonSpeciesById(
        @Path("pokemonId") pokemonId: Int
    ): PokemonSpeciesResponse

    @GET("evolution-chain/{evolutionChainId}")
    suspend fun getEvolutionChainById(
        @Path("evolutionChainId") evolutionChainId: Int
    ): EvolutionChainResponse

    companion object {
        const val BASE_URL = "https://pokeapi.co/api/v2/"
    }
}