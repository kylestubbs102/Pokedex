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
import com.example.pokedex.domain.interfaces.ImageLoader
import com.example.pokedex.util.Resource
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class PokemonDetailEvolutionFragment : Fragment() {

    private val viewModel: PokemonDetailEvolutionViewModel by viewModel()

    private var _binding: FragmentPokemonDetailEvolutionBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var pokemonDetailEvolutionAdapter: PokemonDetailEvolutionAdapter

    private val imageLoader: ImageLoader by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonDetailEvolutionBinding.inflate(inflater, container, false)

        pokemonDetailEvolutionAdapter = PokemonDetailEvolutionAdapter(imageLoader)

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
                collectPokemonEvolutionFlow()
            }
        }
    }

    private suspend fun collectPokemonEvolutionFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
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
        }

        val id = arguments?.getInt(POKEMON_ID_KEY)
        if (id != null) {
            viewModel.fetchPokemonEvolutionList(id)
        } else {
            showErrorScreen()
        }
    }

    private fun showErrorScreen() {
        // TODO : maybe use shimmer
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
        fun newInstance(pokemonId: Int): PokemonDetailEvolutionFragment {
            val args = Bundle().apply {
                putInt(POKEMON_ID_KEY, pokemonId)
            }
            return PokemonDetailEvolutionFragment().apply {
                arguments = args
            }
        }

        private const val ITEM_DECORATOR_ALPHA = 60

        private const val POKEMON_ID_KEY = "pokemon_id_key"
    }
}