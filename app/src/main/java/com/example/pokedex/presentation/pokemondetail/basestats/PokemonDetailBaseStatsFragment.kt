package com.example.pokedex.presentation.pokemondetail.basestats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokedex.databinding.FragmentPokemonDetailBaseStatsBinding
import com.example.pokedex.domain.model.BaseStat
import com.example.pokedex.util.Resource
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PokemonDetailBaseStatsFragment : Fragment() {

    private val viewModel: PokemonDetailBaseStatsViewModel by viewModel()

    private var _binding: FragmentPokemonDetailBaseStatsBinding? = null
    private val binding
        get() = _binding!!

    private var baseStatsAdapter: BaseStatsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonDetailBaseStatsBinding.inflate(inflater, container, false)

        baseStatsAdapter = BaseStatsAdapter()

        binding.recyclerViewBaseStats.apply {
            adapter = baseStatsAdapter
            layoutManager = LinearLayoutManager(context)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel
                    .baseStatsStateFlow
                    .collect { baseStatsState ->
                        when (baseStatsState) {
                            is Resource.Loading -> {
                                // TODO : maybe use shimmer
                            }
                            is Resource.Success -> {
                                baseStatsState.data?.let { setupBaseStats(it) }
                            }
                            is Resource.Error -> {
                                // TODO : error handled in parent fragment
                            }
                        }
                    }
            }
        }

        val id = arguments?.getInt(POKEMON_ID_KEY)
        if (id != null) {
            viewModel.getBaseStats(id)
        } else {
            showErrorScreen()
        }
    }

    private fun setupBaseStats(baseStatList: List<BaseStat>) {
        if (baseStatList.isEmpty()) {
            showErrorScreen()
        }

        // Add custom element to show the total values
        val newList = baseStatList + BaseStat(
            statId = 0,
            pokemonId = 0,
            name = "Total",
            value = baseStatList.sumOf { it.value }
        )

        baseStatsAdapter?.submitList(newList)
    }


    private fun showErrorScreen() {
        // TODO : make error screen
    }

    override fun onDestroyView() {
        super.onDestroyView()
        baseStatsAdapter = null
        _binding = null
    }

    companion object {
        fun newInstance(pokemonId: Int): PokemonDetailBaseStatsFragment {
            val args = Bundle().apply {
                putInt(POKEMON_ID_KEY, pokemonId)
            }
            return PokemonDetailBaseStatsFragment().apply {
                arguments = args
            }
        }

        private const val POKEMON_ID_KEY = "pokemon_id_key"
    }
}