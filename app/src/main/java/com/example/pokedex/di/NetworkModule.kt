package com.example.pokedex.di

import com.example.pokedex.data.remote.PokedexApi
import com.example.pokedex.data.remote.PokedexApi.Companion.BASE_URL
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

val networkModule = module {
    single { provideLoggingInterceptor() }
    single { provideOkHttpClient(get()) }
    single { provideObjectMapper() }
    single { provideRetrofit(get(), get()) }
    single { provideSectorApi(get()) }
}

private fun provideLoggingInterceptor(): HttpLoggingInterceptor =
    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS)

private fun provideOkHttpClient(
    httpLoggingInterceptor: HttpLoggingInterceptor
): OkHttpClient =
    OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .build()

private fun provideObjectMapper(): ObjectMapper =
    ObjectMapper().apply {
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

private fun provideRetrofit(
    okHttpClient: OkHttpClient,
    objectMapper: ObjectMapper,
): Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(JacksonConverterFactory.create(objectMapper))
    .client(okHttpClient)
    .build()

private fun provideSectorApi(
    retrofit: Retrofit
): PokedexApi = retrofit.create(PokedexApi::class.java)