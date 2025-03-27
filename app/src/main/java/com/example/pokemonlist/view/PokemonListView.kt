package com.example.pokemonlist.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokemonlist.data.Prefs
import com.example.pokemonlist.data.UserApplicationShared.Companion.prefs
import com.example.pokemonlist.databinding.ActivityPokemonListBinding
import com.example.pokemonlist.view.adapter.PokemonAdapter
import com.example.pokemonlist.viewmodel.PokemonListViewModel
import com.google.firebase.auth.FirebaseAuth


class PokemonListView : AppCompatActivity() {

    private lateinit var binding: ActivityPokemonListBinding

    companion object {
        const val EXTRA_ID = "Juan"
    }

    private lateinit var adapter: PokemonAdapter
    private val viewModel: PokemonListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPokemonListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar el botón de cierre de sesión
        binding.btnLogOut.setOnClickListener {
            prefs.wipe()
            onBackPressed()
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
        initUI()
        // Configura el SearchView
        setupSearchView()
        // Obtén la lista inicial de Pokémon
        binding.progressBar.isVisible = true
        viewModel.fetchPokemonList()

        initViewModel()
    }

    private fun initViewModel(){
        // Observa los cambios en la lista filtrada
        viewModel.filteredPokemonList.observe(this, { list ->
            adapter.updateList(list)
            binding.progressBar.isVisible = false
        })
    }

    private fun initUI() {
        // Configura el RecyclerView
        binding.rvListPokemon.layoutManager = LinearLayoutManager(this)
        adapter = PokemonAdapter(emptyList()){clickOnDetail(it)}
        binding.rvListPokemon.adapter = adapter
    }

    private fun clickOnDetail(id: String){
        val intent = Intent(this, PokemonDetail::class.java)
        intent.putExtra("EXTRA_ID", id)
        startActivity(intent)
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Filtra la lista cuando el usuario presiona "Enter"
                binding.progressBar.isVisible = true
                query?.let { viewModel.filterPokemonList(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Filtra la lista mientras el usuario escribe
                newText?.let { viewModel.filterPokemonList(it) }
                return true
            }
        })
    }

}
