package com.example.pokemonlist.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.pokemonlist.databinding.ActivityPokemonDetailBinding
import com.example.pokemonlist.model.PokemonCard
import com.example.pokemonlist.viewmodel.PokemonListViewModel

class PokemonDetail : AppCompatActivity() {

    private lateinit var binding: ActivityPokemonDetailBinding
    private val viewModel: PokemonListViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPokemonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = intent.getStringExtra("EXTRA_ID") ?: ""

        viewModel.fetchPokemonDetails(id)
        viewModel.pokemonDetails.observe(this, { pokemon ->
            createUI(pokemon)
        })

    }

    private fun createUI(pokemonDetails: PokemonCard){
        binding.tvPokemonName.text = pokemonDetails.name
        binding.tvPokemonAbilities.text = pokemonDetails.nameAbilitys
        Glide.with(binding.imgPokemon.context).load(pokemonDetails.imageUrl).into(binding.imgPokemon)
    }
}