package com.example.pokemonlist.model

import com.google.gson.annotations.SerializedName

data class PokemonListResponse(@SerializedName("results") val results: List<PokemonResult>)

data class PokemonResult(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)

data class PokemonResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("sprites") val sprites: Sprites,
    @SerializedName("abilities") val abilities: List<Ability>
)

data class Ability(@SerializedName("ability") val ability: AbilityInfo)
data class AbilityInfo(@SerializedName("name") val name: String)

data class Sprites(@SerializedName("front_default") val frontDefault: String) //Url de la imagen



