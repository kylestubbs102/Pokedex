package com.example.pokedex.data.repository

import com.example.pokedex.data.local.PokedexDao
import com.example.pokedex.data.local.PokedexDatabase
import com.example.pokedex.data.local.PokemonCardInfoEntity
import com.example.pokedex.data.local.PokemonInfoEntity
import com.example.pokedex.data.mappers.toBaseStat
import com.example.pokedex.data.mappers.toBaseStatEntityList
import com.example.pokedex.data.mappers.toPokemonCardInfo
import com.example.pokedex.data.mappers.toPokemonEvolution
import com.example.pokedex.data.mappers.toPokemonEvolutionEntityList
import com.example.pokedex.data.mappers.toPokemonInfo
import com.example.pokedex.data.mappers.toPokemonInfoEntity
import com.example.pokedex.data.remote.PokedexApi
import com.example.pokedex.data.remote.dto.pokemonspecies.PokemonSpeciesResponse
import com.example.pokedex.domain.model.BaseStat
import com.example.pokedex.domain.model.PokemonCardInfo
import com.example.pokedex.domain.model.PokemonEvolution
import com.example.pokedex.domain.model.PokemonInfo
import com.example.pokedex.domain.repository.PokedexRepository
import com.example.pokedex.util.Helpers
import com.example.pokedex.util.Resource
import java.io.IOException
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class PokedexRepositoryImpl(
    pokedexDatabase: PokedexDatabase,
    private val pokedexApi: PokedexApi,
) : PokedexRepository {

    private val pokedexDao: PokedexDao = pokedexDatabase.pokedexDao

    override suspend fun getPokemonCardInfoList(
        limit: Int,
        offset: Int,
        isInitialFetch: Boolean,
    ): Flow<Resource<List<PokemonCardInfo>>> = flow {
        emit(Resource.Loading())

        val doesPokemonCardInfoListExist =
            pokedexDao.checkIfPokemonExistsWithLimitAndOffset(limit, offset)

        if (doesPokemonCardInfoListExist) {
            val pokemonCardInfoList = pokedexDao
                .selectAllPokemon()
                .map { it.toPokemonCardInfo() }
            emit(Resource.Success(data = pokemonCardInfoList))
            return@flow
        }

        try {
            val pokemonCardInfoList = fetchPokemonCardData(limit, offset)

            pokedexDao.insertPokemonCardInfoEntityList(pokemonCardInfoList)

            emit(
                Resource.Success(pokedexDao.selectPokemonWithLimitAndOffset(
                    limit,
                    offset
                ).map { pokemonCardInfoEntity ->
                    pokemonCardInfoEntity.toPokemonCardInfo()
                })
            )

        } catch (e: HttpException) {
            e.printStackTrace()
            emit(Resource.Error(message = e.message()))
        } catch (e: IOException) {
            e.printStackTrace()
            emit(Resource.Error(message = e.toString()))
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun fetchPokemonCardData(
        limit: Int,
        offset: Int
    ): List<PokemonCardInfoEntity> = withContext(Dispatchers.IO) {
        val pokemonCardInfoListResponse = pokedexApi.getPokemonListByLimitAndOffset(
            limit,
            offset
        )

        val pokemonSpeciesDeferredList = mutableListOf<Deferred<PokemonSpeciesResponse>>()
        for (index in 0..pokemonCardInfoListResponse.results.size) {
            val id = index + offset + 1
            if (id > 905) break

            val pokemonSpeciesJob = async { pokedexApi.getPokemonSpeciesById(id) }

            pokemonSpeciesDeferredList.add(pokemonSpeciesJob)
        }
        val pokemonSpeciesResponseList = pokemonSpeciesDeferredList.awaitAll()

        val pokemonCardInfoList = pokemonCardInfoListResponse.results.mapIndexed { index, result ->
            var id = index + offset + 1
            if (id > 905) {
                id += 9095      // pokemon list id changes to 10001 after 905
            }
            val color = when (id <= 905) {
                true -> pokemonSpeciesResponseList[index].color.name
                false -> ""     // will become default color
            }
            PokemonCardInfoEntity(
                id = id,
                name = result.name,
                imageUrl = Helpers.getImageUrl(id),
                color = color
            )
        }
        return@withContext pokemonCardInfoList
    }

    override suspend fun getPokemonInfoById(
        id: Int
    ): Flow<Resource<PokemonInfo>> = flow {
        emit(Resource.Loading())

        val pokemonInfoEntity: PokemonInfoEntity? = pokedexDao.selectPokemonWithId(id)
        pokemonInfoEntity?.let {
            emit(Resource.Success(it.toPokemonInfo()))
            return@flow
        }

        try {
            fetchAndSavePokemonInfoAndBaseStats(id)

            val pokemonInfo = pokedexDao.selectPokemonWithId(id)?.let {
                it.toPokemonInfo()
            }
            if (pokemonInfo == null) {
                emit(Resource.Error(message = "Pokemon database insertion failed."))
            } else {
                emit(Resource.Success(pokemonInfo))
            }
        } catch (e: HttpException) {
            e.printStackTrace()
            emit(Resource.Error(message = e.message()))
        } catch (e: IOException) {
            e.printStackTrace()
            emit(Resource.Error(message = e.toString()))
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun fetchAndSavePokemonInfoAndBaseStats(
        id: Int
    ) = withContext(Dispatchers.IO) {
        val pokemonSpeciesJob = async { pokedexApi.getPokemonSpeciesById(id) }
        val pokemonInfoJob = async { pokedexApi.getPokemonInfoById(id) }
        val pokemonSpeciesResponse = pokemonSpeciesJob.await()
        val pokemonInfoResponse = pokemonInfoJob.await()

        val pokemonInfoEntity = pokemonInfoResponse.toPokemonInfoEntity(pokemonSpeciesResponse)
        val baseStatList = pokemonInfoResponse.toBaseStatEntityList()

        async { pokedexDao.insertPokemonInfoEntity(pokemonInfoEntity) }
        async { pokedexDao.insertBaseStatEntityList(baseStatList) }
    }.await()

    override suspend fun updatePokemonIsLiked(id: Int, isLiked: Boolean) {
        pokedexDao.updatePokemonIsLiked(id, isLiked)
    }

    override suspend fun fetchEvolutionChain(
        id: Int,
        evolutionChainId: Int,
    ): Flow<Resource<List<PokemonEvolution>>> = flow {
        emit(Resource.Loading())

        val dbEvolutionList = withContext(Dispatchers.IO) {
            pokedexDao.selectPokemonEvolutionListWithEvolutionId(evolutionChainId)
        }

        if (dbEvolutionList.isNotEmpty()) {
            emit(Resource.Success(
                dbEvolutionList.map { it.toPokemonEvolution() }
            ))
            return@flow
        }

        try {
            val evolutionChainResponse = pokedexApi.getEvolutionChainById(evolutionChainId)

            pokedexDao.insertPokemonEvolutionEntityList(
                evolutionChainResponse
                    .toPokemonEvolutionEntityList()
            )

            emit(
                Resource.Success(
                    pokedexDao
                        .selectPokemonEvolutionListWithEvolutionId(evolutionChainId)
                        .map { it.toPokemonEvolution() }
                )
            )
        } catch (e: HttpException) {
            e.printStackTrace()
            emit(Resource.Error(message = e.message()))
        } catch (e: IOException) {
            e.printStackTrace()
            emit(Resource.Error(message = e.toString()))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun fetchBaseStatsById(pokemonId: Int): Flow<Resource<List<BaseStat>>> =
        flow {
            try {
                emit(Resource.Loading())
                pokedexDao
                    .selectBaseStatListWithPokemonId(pokemonId)
                    .collect {
                        val baseStatList = it.map { bsEntity -> bsEntity.toBaseStat() }
                        emit(Resource.Success(data = baseStatList))
                    }
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = e.toString()))
            }
        }

}