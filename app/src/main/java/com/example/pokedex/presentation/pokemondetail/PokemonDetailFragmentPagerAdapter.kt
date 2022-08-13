package com.example.pokedex.presentation.pokemondetail

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pokedex.presentation.pokemondetail.about.PokemonDetailAboutFragment
import com.example.pokedex.presentation.pokemondetail.basestats.PokemonDetailBaseStatsFragment
import com.example.pokedex.presentation.pokemondetail.evolution.PokemonDetailEvolutionFragment
import com.example.pokedex.presentation.pokemondetail.moves.PokemonDetailMovesFragment

class PokemonDetailFragmentPagerAdapter(
    fragment: PokemonDetailFragment,
    private val pokemonId: Int
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            ABOUT_POSITION -> PokemonDetailAboutFragment.newInstance()
            BASE_STATS_POSITION -> PokemonDetailBaseStatsFragment.newInstance(pokemonId)
            EVOLUTION_POSITION -> PokemonDetailEvolutionFragment.newInstance(pokemonId)
            MOVES_POSITION -> PokemonDetailMovesFragment.newInstance()
            else -> throw IndexOutOfBoundsException("View pager has invalid position.")
        }
    }

    companion object {
        const val ABOUT_POSITION = 0
        const val BASE_STATS_POSITION = 1
        const val EVOLUTION_POSITION = 2
        const val MOVES_POSITION = 3

        private const val NUM_PAGES = 4
    }

}