package com.example.pokedex.data.interfaces_impl

import com.example.pokedex.data.local.PokedexDao
import com.example.pokedex.data.local.PokedexDatabase
import com.example.pokedex.data.local.PokemonInfoEntity
import com.example.pokedex.data.mappers.PokemonAboutInfoMapper
import com.example.pokedex.data.mappers.toBaseStat
import com.example.pokedex.data.mappers.toBaseStatEntityList
import com.example.pokedex.data.mappers.toPokemonEvolution
import com.example.pokedex.data.mappers.toPokemonEvolutionEntityList
import com.example.pokedex.data.mappers.toPokemonInfo
import com.example.pokedex.data.mappers.toPokemonInfoEntity
import com.example.pokedex.data.mappers.toPokemonSpeciesEntity
import com.example.pokedex.data.remote.PokedexApi
import com.example.pokedex.data.remote.dto.pokemoninfo.PokemonInfoResponse
import com.example.pokedex.domain.interfaces.PokedexRepository
import com.example.pokedex.domain.model.BaseStat
import com.example.pokedex.domain.model.PokemonAboutInfo
import com.example.pokedex.domain.model.PokemonEvolution
import com.example.pokedex.domain.model.PokemonInfo
import com.example.pokedex.util.Resource
import java.io.IOException
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class PokedexRepositoryImpl(
    pokedexDatabase: PokedexDatabase,
    private val pokedexApi: PokedexApi,
) : PokedexRepository {

    private val pokedexDao: PokedexDao = pokedexDatabase.pokedexDao

    override suspend fun getPokemonInfoList(
        limit: Int,
        offset: Int,
    ): Flow<Resource<List<PokemonInfo>>> = flow {
        emit(Resource.Loading())

        val doesPokemonInfoListExist =
            pokedexDao.checkIfPokemonExistsWithLimitAndOffset(limit, offset)

        if (doesPokemonInfoListExist) {
            val pokemonInfoList = pokedexDao
                .selectAllPokemon()
                .map { it.toPokemonInfo() }
            emit(Resource.Success(data = pokemonInfoList))
            return@flow
        }

        try {
            val pokemonInfoList = fetchPokemonInfoList(limit, offset)

            pokedexDao.insertPokemonInfoEntityList(pokemonInfoList)

            emit(
                Resource.Success(pokedexDao.selectPokemonWithLimitAndOffset(
                    limit,
                    offset
                ).map { pokemonInfoEntity ->
                    pokemonInfoEntity.toPokemonInfo()
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

    private suspend fun fetchPokemonInfoList(
        limit: Int,
        offset: Int
    ): List<PokemonInfoEntity> = withContext(Dispatchers.IO) {
        val pokemonListResponse = pokedexApi.getPokemonListByLimitAndOffset(
            limit,
            offset
        )

        val pokemonInfoDeferredList = mutableListOf<Deferred<PokemonInfoResponse>>()
        for (index in 0..pokemonListResponse.results.size) {
            val id = index + offset + 1
            if (id > 905) break

            val pokemonInfoJob = async { pokedexApi.getPokemonInfoById(id) }

            pokemonInfoDeferredList.add(pokemonInfoJob)
        }
        val pokemonInfoResponseList = pokemonInfoDeferredList.awaitAll()

        val pokemonInfoList = pokemonListResponse.results.mapIndexed { index, result ->
            var id = index + offset + 1
            if (id > 905) {
                id += 9095      // pokemon list id changes to 10001 after 905
            }

            val currentPokemonInfoResponse = pokemonInfoResponseList[index]
            launch {
                val baseStatEntityList = currentPokemonInfoResponse.toBaseStatEntityList()
                pokedexDao.insertBaseStatEntityList(baseStatEntityList)
            }

            PokemonInfoEntity(
                id = id,
                name = result.name,
                types = currentPokemonInfoResponse.types.map { it.type.name },
                height = currentPokemonInfoResponse.height,
                weight = currentPokemonInfoResponse.weight,
                baseExperience = currentPokemonInfoResponse.baseExperience,
                isLiked = false
            )
        }
        return@withContext pokemonInfoList
    }

    override suspend fun updatePokemonIsLiked(id: Int, isLiked: Boolean) {
        pokedexDao.updatePokemonIsLiked(id, isLiked)
    }

    override suspend fun getEvolutionChain(id: Int): Flow<Resource<List<PokemonEvolution>>> =
        flow {
            emit(Resource.Loading())

            pokedexDao.selectEvolutionChainIdWithId(id)
                .collect {
                    val result = evolutionChainHelper(it)
                    emit(result)
                }
        }.flowOn(Dispatchers.IO)

    private suspend fun evolutionChainHelper(
        evolutionChainId: Int?
    ): Resource<List<PokemonEvolution>> = coroutineScope {

        if (evolutionChainId == null) {
            return@coroutineScope Resource.Success(emptyList<PokemonEvolution>())
        }

        val dbEvolutionList = withContext(Dispatchers.IO) {
            pokedexDao.selectPokemonEvolutionListWithEvolutionId(evolutionChainId)
        }

        if (dbEvolutionList.isNotEmpty()) {
            return@coroutineScope Resource.Success(
                dbEvolutionList.map { it.toPokemonEvolution() }
            )
        }

        try {
            val evolutionChainResponse = pokedexApi.getEvolutionChainById(evolutionChainId)

            pokedexDao.insertPokemonEvolutionEntityList(
                evolutionChainResponse
                    .toPokemonEvolutionEntityList()
            )

            return@coroutineScope Resource.Success(
                pokedexDao
                    .selectPokemonEvolutionListWithEvolutionId(evolutionChainId)
                    .map { it.toPokemonEvolution() }
            )


        } catch (e: HttpException) {
            e.printStackTrace()
            return@coroutineScope Resource.Error(message = e.message())
        } catch (e: IOException) {
            e.printStackTrace()
            return@coroutineScope Resource.Error(message = e.toString())
        }
    }

    override suspend fun getBaseStatsById(pokemonId: Int): Flow<Resource<List<BaseStat>>> =
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

    override suspend fun getPokemonAboutInfoById(
        pokemonId: Int
    ): Flow<Resource<PokemonAboutInfo>> = flow {
        try {
            emit(Resource.Loading())
            val pokemonInfoEntity = pokedexDao.selectPokemonInfoWithId(pokemonId)
            val pokemonSpeciesEntity = pokedexDao.selectPokemonSpeciesWithId(pokemonId)
            if (pokemonInfoEntity != null && pokemonSpeciesEntity != null) {
                val pokemonAboutInfo = PokemonAboutInfoMapper.toPokemonAboutInfo(
                    pokemonInfoEntity,
                    pokemonSpeciesEntity
                )
                emit(Resource.Success(pokemonAboutInfo))
                return@flow
            }

            coroutineScope {
                if (pokemonInfoEntity == null) {
                    launch {
                        val pokemonInfoResponse = pokedexApi.getPokemonInfoById(pokemonId)
                        pokedexDao.insertPokemonInfoEntity(pokemonInfoResponse.toPokemonInfoEntity())
                    }
                }
                if (pokemonSpeciesEntity == null) {
                    launch {
                        val pokemonSpeciesResponse = pokedexApi.getPokemonSpeciesById(pokemonId)
                        pokedexDao.insertPokemonSpeciesEntity(pokemonSpeciesResponse.toPokemonSpeciesEntity())
                    }
                }
            }
            val newPokemonInfoEntity = pokedexDao.selectPokemonInfoWithId(pokemonId)
            val newPokemonSpeciesEntity = pokedexDao.selectPokemonSpeciesWithId(pokemonId)
            if (newPokemonInfoEntity != null && newPokemonSpeciesEntity != null) {
                val pokemonAboutInfo = PokemonAboutInfoMapper.toPokemonAboutInfo(
                    newPokemonInfoEntity,
                    newPokemonSpeciesEntity
                )
                emit(Resource.Success(pokemonAboutInfo))
            }
        } catch (e: HttpException) {
            e.printStackTrace()
            emit(Resource.Error(message = e.message()))
        } catch (e: IOException) {
            e.printStackTrace()
            emit(Resource.Error(message = e.toString()))
        }
    }

}