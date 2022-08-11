package com.example.pokedex.presentation.pokemondetail.evolution

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.databinding.PokemonEvolutionItemBinding
import com.example.pokedex.domain.interfaces.ImageLoader
import com.example.pokedex.domain.model.PokemonEvolution
import com.example.pokedex.util.Helpers

class PokemonDetailEvolutionAdapter(
    private val imageLoader: ImageLoader
) : ListAdapter<PokemonEvolution, PokemonDetailEvolutionAdapter.PokemonEvolutionViewHolder>(
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonEvolutionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PokemonEvolutionItemBinding.inflate(layoutInflater, parent, false)
        return PokemonEvolutionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonEvolutionViewHolder, position: Int) {
        val pokemonEvolution = getItem(position)
        holder.bind(pokemonEvolution)
    }

    inner class PokemonEvolutionViewHolder(
        private val binding: PokemonEvolutionItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pokemonEvolution: PokemonEvolution) {
            pokemonEvolution.let {
                setTextViews(it)
                setImagesLoaders(it)
            }
        }

        private fun setTextViews(
            pokemonEvolution: PokemonEvolution
        ) {
            binding.apply {
                textViewOriginalPokemon.text = pokemonEvolution.name
                textViewEvolvedPokemon.text = pokemonEvolution.evolvedSpeciesName

                if (pokemonEvolution.minLevel == null) {
                    textViewMinLevel.text = ""
                } else {
                    textViewMinLevel.text = root.context.getString(
                        R.string.text_view_min_level,
                        pokemonEvolution.minLevel
                    )
                }
            }
        }

        private fun setImagesLoaders(
            pokemonEvolution: PokemonEvolution
        ) {
            val originalUrl = Helpers.getImageUrl(pokemonEvolution.id)
            val evolvedUrl = Helpers.getImageUrl(pokemonEvolution.evolvedSpeciesId)

            with(binding) {
                imageLoader.loadImage(
                    url = originalUrl,
                    imageView = imageViewOriginalPokemon
                )
                imageLoader.loadImage(
                    url = evolvedUrl,
                    imageView = imageViewEvolvedPokemon
                )
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PokemonEvolution>() {
            override fun areItemsTheSame(
                oldItem: PokemonEvolution,
                newItem: PokemonEvolution
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: PokemonEvolution,
                newItem: PokemonEvolution
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}