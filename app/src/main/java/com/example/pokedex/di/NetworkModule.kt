package com.example.pokedex.di

import com.example.pokedex.data.remote.PokedexApi
import com.example.pokedex.data.remote.PokedexApi.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

val networkModule = module {
    single { provideLoggingInterceptor() }
    single { provideOkHttpClient(get()) }
    single { provideRetrofit(get()) }
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

private fun provideRetrofit(
    okHttpClient: OkHttpClient
): Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(JacksonConverterFactory.create())
    .client(okHttpClient)
    .build()

private fun provideSectorApi(
    retrofit: Retrofit
): PokedexApi = retrofit.create(PokedexApi::class.java)