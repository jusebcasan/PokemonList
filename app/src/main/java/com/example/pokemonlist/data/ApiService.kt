package com.example.pokemonlist.data


import com.example.pokemonlist.model.PokemonListResponse
import com.example.pokemonlist.model.PokemonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("pokemon/{name}")
    suspend fun getPokemonByName(@Path("name") name: String): Response<PokemonResponse>

    @GET("pokemon")
    suspend fun getPokemonList(@Query("limit") limit: Int, @Query("offset") offset: Int): Response<PokemonListResponse>

    @GET("pokemon/{id}")
    suspend fun getPokemonDetails(@Path("id") id: String): Response<PokemonResponse>

}