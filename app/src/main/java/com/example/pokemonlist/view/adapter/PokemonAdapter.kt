package com.example.pokemonlist.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemonlist.view.viewholder.PokemonViewHolder
import com.example.pokemonlist.R
import com.example.pokemonlist.model.Pokemon


class PokemonAdapter(private var pokemonList: List<Pokemon> = emptyList(), private val onItemClick: (String) -> Unit) :
    RecyclerView.Adapter<PokemonViewHolder>() {

        fun updateList(newList: List<Pokemon>){
            pokemonList = newList
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        return PokemonViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_pokemonlist, parent, false)
        )
    }

    override fun getItemCount() = pokemonList.size


    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bind(pokemonList[position], onItemClick)
    }
}
