package com.example.pokemonlist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonlist.data.ApiService
import com.example.pokemonlist.model.Pokemon
import com.example.pokemonlist.model.PokemonCard
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokemonListViewModel : ViewModel() {

    private val _filteredPokemonList = MutableLiveData<List<Pokemon>>()
    val filteredPokemonList: LiveData<List<Pokemon>> get() = _filteredPokemonList

    private val _pokemonList = MutableLiveData<List<Pokemon>>()
    val pokemonList: LiveData<List<Pokemon>> get() = _pokemonList

    private val _pokemonDetails = MutableLiveData<PokemonCard>()
    val pokemonDetails: LiveData<PokemonCard> get() = _pokemonDetails

    private val apiservice: ApiService by lazy {
        Retrofit
            .Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }


    fun fetchPokemonList(limit: Int = 25, offset: Int = 0) {
        viewModelScope.launch {
            try {
                val response = apiservice.getPokemonList(limit, offset)
                if (response.isSuccessful) {
                    val pokemonResults = response.body()?.results ?: emptyList()
                    val pokemonList = pokemonResults.map { result ->
                        val detailsResponse = apiservice.getPokemonByName(result.name)
                        if (detailsResponse.isSuccessful) {
                            val details = detailsResponse.body()!!
                            Pokemon(
                                id = details.id,
                                name = details.name,
                                imageUrl = details.sprites.frontDefault
                            )
                        } else {
                            null
                        }
                    }.filterNotNull()
                    _pokemonList.postValue(pokemonList)
                    _filteredPokemonList.postValue(pokemonList)// inicializa la lista filtrada con la lista completa
                }
            } catch (e: Exception) {
                // Maneja el error
            }
        }
    }

    fun filterPokemonList(query: String) {
        val currentList = _pokemonList.value ?: emptyList()
        if (query.isEmpty()) {
            _filteredPokemonList.postValue(currentList) // Muestra la lista completa si no hay consulta
        } else {
            val filteredList = currentList.filter { pokemon ->
                pokemon.name.contains(query, ignoreCase = true) // Filtra por nombre
            }
            _filteredPokemonList.postValue(filteredList)
        }
    }

    fun fetchPokemonDetails(id: String) {
        viewModelScope.launch {
            try {
                val response = apiservice.getPokemonDetails(id)
                if (response.isSuccessful) {
                    val details = response.body()!!
                    val pokemon = PokemonCard(
                        name = details.name,
                        nameAbilitys = details.abilities.joinToString { it.ability.name },
                        imageUrl = details.sprites.frontDefault
                    )
                    _pokemonDetails.postValue(pokemon)
                }
            }catch (e: Exception){
                // Maneja el error
            }
        }
    }
}

