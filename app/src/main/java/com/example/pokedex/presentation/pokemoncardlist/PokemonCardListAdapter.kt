package com.example.pokedex.presentation.pokemoncardlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.pokedex.databinding.PokemonCardItemBinding
import com.example.pokedex.domain.model.PokemonCardInfo
import com.example.pokedex.util.PokemonColorUtils
import java.util.Locale

class PokemonCardListAdapter(
    private val cardToDetailFragmentTransaction: (pokemonCardInfo: PokemonCardInfo) -> Unit
) : ListAdapter<PokemonCardInfo, PokemonCardListAdapter.PokemonCardViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonCardViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PokemonCardItemBinding.inflate(layoutInflater, parent, false)
        return PokemonCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonCardViewHolder, position: Int) {
        val pokemonCardInfo = getItem(position)
        holder.bind(pokemonCardInfo)
    }

    inner class PokemonCardViewHolder(
        private val binding: PokemonCardItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(pokemonCardInfo: PokemonCardInfo) {
            pokemonCardInfo.let {
                setCardText(it)
                setCardColors(it)
                setupImageLoader(it)
                setupOnClickListener(it)
            }
        }

        private fun setCardColors(
            pokemonCardInfo: PokemonCardInfo
        ) {
            binding.apply {
                textViewPokemonCardId.setTextColor(
                    ContextCompat.getColor(
                        root.context,
                        PokemonColorUtils.getPokemonTextColor(pokemonCardInfo.color)
                    )
                )

                textViewPokemonCardName.setTextColor(
                    ContextCompat.getColor(
                        root.context,
                        PokemonColorUtils.getPokemonTextColor(pokemonCardInfo.color)
                    )
                )

                root.setCardBackgroundColor(
                    ContextCompat.getColor(
                        root.context,
                        PokemonColorUtils.getPokemonColor(pokemonCardInfo.color)
                    )
                )
            }
        }

        private fun setCardText(pokemonCardInfo: PokemonCardInfo) {
            binding.apply {
                textViewPokemonCardId.text = pokemonCardInfo
                    .id
                    .toString()
                    .padStart(3, '0')
                    .prependIndent("#")

                textViewPokemonCardName.text = pokemonCardInfo.name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.ROOT
                    ) else it.toString()
                }
            }
        }

        private fun setupImageLoader(
            pokemonCardInfo: PokemonCardInfo
        ) {
            binding.apply {
                Glide
                    .with(root.context)
                    .load(pokemonCardInfo.imageUrl)
                    .placeholder(CircularProgressDrawable(root.context).apply {
                        strokeWidth = 5f
                        centerRadius = 30f
                        start()
                    })
                    .error(com.google.android.material.R.drawable.mtrl_ic_error)
                    .into(imageViewPokemon)
            }
        }

        private fun setupOnClickListener(
            pokemonCardInfo: PokemonCardInfo
        ) {
            binding.apply {
                root.setOnClickListener {
                    cardToDetailFragmentTransaction(pokemonCardInfo)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PokemonCardInfo>() {
            override fun areItemsTheSame(
                oldItem: PokemonCardInfo,
                newItem: PokemonCardInfo
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: PokemonCardInfo,
                newItem: PokemonCardInfo
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
