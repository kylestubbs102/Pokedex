package com.example.pokedex.presentation.pokemondetail.evolution

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.databinding.FragmentPokemonDetailEvolutionBinding
import com.example.pokedex.presentation.pokemondetail.PokemonDetailViewModel
import com.example.pokedex.util.Resource
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel


class PokemonDetailEvolutionFragment : Fragment() {

    private val viewModel by lazy {
        requireParentFragment().getViewModel<PokemonDetailViewModel>()
    }

    private var _binding: FragmentPokemonDetailEvolutionBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var pokemonDetailEvolutionAdapter: PokemonDetailEvolutionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonDetailEvolutionBinding.inflate(inflater, container, false)

        pokemonDetailEvolutionAdapter = PokemonDetailEvolutionAdapter()

        val itemDecoration = setupItemDecoration()

        binding.recyclerViewEvolutionList.apply {
            adapter = pokemonDetailEvolutionAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(itemDecoration)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    collectPokemonInfoFlow()
                }
                launch {
                    collectPokemonEvolutionFlow()
                }
            }
        }
    }

    private suspend fun collectPokemonInfoFlow() {
        viewModel.pokemonInfoFlow
            .collect { pokemonEvolutionState ->
                when (pokemonEvolutionState) {
                    is Resource.Loading -> {
                        // TODO : maybe use shimmer
                    }
                    is Resource.Success -> {
                        if (pokemonEvolutionState.data?.evolutionChainId == null) {
                            binding.textViewTitle.text =
                                getString(R.string.text_view_no_evolution_chain)
                            return@collect
                        }

                        viewModel.fetchPokemonEvolutionList(
                            pokemonEvolutionState.data.id,
                            pokemonEvolutionState.data.evolutionChainId
                        )
                    }
                    is Resource.Error -> {
                        // TODO : error handled in parent fragment
                    }
                }
            }
    }

    private suspend fun collectPokemonEvolutionFlow() {
        viewModel.pokemonEvolutionFlow
            .collect { pokemonEvolutionState ->
                when (pokemonEvolutionState) {
                    is Resource.Loading -> {
                        // TODO : maybe use shimmer
                    }
                    is Resource.Success -> {
                        if (pokemonEvolutionState.data.isNullOrEmpty()) {
                            binding.textViewTitle.text =
                                getString(R.string.text_view_no_evolution_chain)
                            return@collect
                        }

                        pokemonDetailEvolutionAdapter.submitList(pokemonEvolutionState.data)
                    }
                    is Resource.Error -> {
                        // TODO : error handled in parent fragment
                    }
                }
            }
    }

    private fun setupItemDecoration(): DividerItemDecoration {
        val drawable =
            AppCompatResources.getDrawable(requireContext(), R.drawable.recycler_view_divider)
        drawable?.alpha = ITEM_DECORATOR_ALPHA
        val itemDecorator = object : DividerItemDecoration(context, VERTICAL) {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val position = parent.getChildAdapterPosition(view)
                // hide the divider for the last child
                if (position == state.itemCount - 1) {
                    outRect.setEmpty()
                } else {
                    super.getItemOffsets(outRect, view, parent, state)
                }
            }
        }
        if (drawable != null) {
            itemDecorator.setDrawable(drawable)
        }
        return itemDecorator
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PokemonDetailEvolutionFragment()

        private const val ITEM_DECORATOR_ALPHA = 60
    }
}