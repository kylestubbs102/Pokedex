package com.example.pokedex.presentation.pokemondetail.basestats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.BaseStatRowBinding
import com.example.pokedex.domain.model.BaseStat

class BaseStatsAdapter :
    ListAdapter<BaseStat, BaseStatsAdapter.BaseStatsViewHolder>(
        DIFF_CALLBACK
    ) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseStatsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = BaseStatRowBinding.inflate(layoutInflater, parent, false)
        return BaseStatsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseStatsViewHolder, position: Int) {
        val baseStat = getItem(position)
        holder.bind(baseStat, position)
    }

    inner class BaseStatsViewHolder(
        private val binding: BaseStatRowBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            baseStat: BaseStat,
            position: Int
        ) {
            binding.apply {
                textViewStatValue.text = baseStat.value.toString()
                textViewStatLabel.text = baseStat.name

                // TODO : change it to pass in start value when ability to swipe is added
                baseStatBar.setIndexAndBarPositions(0F, baseStat.value.toFloat(), position)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BaseStat>() {
            override fun areItemsTheSame(
                oldItem: BaseStat,
                newItem: BaseStat
            ): Boolean {
                return oldItem.statId == newItem.statId && oldItem.pokemonId == newItem.pokemonId
            }

            override fun areContentsTheSame(
                oldItem: BaseStat,
                newItem: BaseStat
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}