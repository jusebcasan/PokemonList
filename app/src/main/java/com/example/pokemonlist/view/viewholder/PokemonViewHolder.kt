package com.example.pokemonlist.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokemonlist.databinding.ItemPokemonlistBinding
import com.example.pokemonlist.model.Pokemon

class PokemonViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemPokemonlistBinding.bind(view)

    fun bind(pokemon: Pokemon, onItemClick: (String) -> Unit) {
        binding.tvPokemonName.text = pokemon.name
        Glide.with(binding.ivPokemon.context).load(pokemon.imageUrl).into(binding.ivPokemon)
        // Configurar el clic en el elemento
        binding.root.setOnClickListener { onItemClick(pokemon.id) }
    }
}
