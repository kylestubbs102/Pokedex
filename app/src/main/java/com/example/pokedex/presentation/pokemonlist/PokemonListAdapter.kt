package com.example.pokedex.presentation.pokemonlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.example.pokedex.databinding.PokemonCardItemBinding
import com.example.pokedex.domain.interfaces.ImageLoader
import com.example.pokedex.domain.model.PokemonInfo
import com.example.pokedex.util.Helpers
import com.example.pokedex.util.PokemonColorUtils
import java.util.Locale

class PokemonListAdapter(
    private val imageLoader: ImageLoader,
    private val cardToDetailFragmentTransaction: (pokemonInfo: PokemonInfo) -> Unit
) : ListAdapter<PokemonInfo, PokemonListAdapter.PokemonCardViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonCardViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PokemonCardItemBinding.inflate(layoutInflater, parent, false)
        return PokemonCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonCardViewHolder, position: Int) {
        val pokemonInfo = getItem(position)
        holder.bind(pokemonInfo)
    }

    inner class PokemonCardViewHolder(
        private val binding: PokemonCardItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(pokemonInfo: PokemonInfo) {
            pokemonInfo.let {
                setCardText(it)
                setCardColors(it)
                setCardTypes(it)
                setupImageLoader(it)
                setupOnClickListener(it)
            }
        }

        private fun setCardColors(pokemonInfo: PokemonInfo) {
            binding.apply {
                root.setCardBackgroundColor(
                    ContextCompat.getColor(
                        root.context,
                        PokemonColorUtils.getPokemonColor(pokemonInfo)
                    )
                )
            }
        }

        private fun setCardText(pokemonInfo: PokemonInfo) {
            binding.apply {
                textViewPokemonCardId.text = pokemonInfo
                    .id
                    .toString()
                    .padStart(3, '0')
                    .prependIndent("#")

                textViewPokemonCardName.text = pokemonInfo.name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.ROOT
                    ) else it.toString()
                }
            }
        }

        private fun setCardTypes(pokemonInfo: PokemonInfo) {
            binding.apply {
                when (pokemonInfo.types.size) {
                    0 -> {
                        textViewType1.visibility = View.INVISIBLE
                        textViewType2.visibility = View.INVISIBLE
                    }
                    1 -> {
                        textViewType1.text =
                            pokemonInfo.types[0].replaceFirstChar { it.uppercase() }
                        textViewType2.visibility = View.INVISIBLE
                    }
                    2 -> {
                        textViewType1.text =
                            pokemonInfo.types[0].replaceFirstChar { it.uppercase() }
                        textViewType2.text =
                            pokemonInfo.types[1].replaceFirstChar { it.uppercase() }
                    }
                    else -> throw IndexOutOfBoundsException("PokemonInfo contains more than two types.")
                }
            }
        }

        private fun setupImageLoader(pokemonInfo: PokemonInfo) {
            binding.apply {
                imageLoader.loadImage(
                    url = Helpers.getImageUrl(pokemonInfo.id),
                    imageView = imageViewPokemon,
                    placeholder = CircularProgressDrawable(root.context).apply {
                        strokeWidth = 5f
                        centerRadius = 30f
                        start()
                    }
                )
            }
        }

        private fun setupOnClickListener(pokemonInfo: PokemonInfo) {
            binding.apply {
                root.setOnClickListener {
                    cardToDetailFragmentTransaction(pokemonInfo)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PokemonInfo>() {
            override fun areItemsTheSame(
                oldItem: PokemonInfo,
                newItem: PokemonInfo
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: PokemonInfo,
                newItem: PokemonInfo
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
