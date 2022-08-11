package com.example.pokedex.presentation.pokemoncardlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.databinding.FragmentPokemonCardListBinding
import com.example.pokedex.domain.interfaces.ImageLoader
import com.example.pokedex.domain.model.PokemonCardInfo
import com.example.pokedex.presentation.pokemondetail.PokemonDetailFragment
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PokemonCardListFragment : Fragment() {

    private val viewModel: PokemonCardListViewModel by viewModel()

    private var _binding: FragmentPokemonCardListBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var pokemonCardListAdapter: PokemonCardListAdapter

    private val imageLoader: ImageLoader by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonCardListBinding.inflate(inflater, container, false)

        setupRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel
                    .pokemonCardInfoListState
                    .collect { pokemonCardListState ->
                        if (pokemonCardListState.errorMessage != "") {
                            // TODO : handle error message
                            return@collect
                        }

                        val listToUpdate = pokemonCardListState.pokemonCardList
                        if (pokemonCardListState.isLoading) {
                            updateAdapterAndProgressBar(View.VISIBLE, listToUpdate)
                        } else {
                            updateAdapterAndProgressBar(View.GONE, listToUpdate)
                        }
                    }
            }
        }
    }

    private fun setupRecyclerView() {
        val scrollListener = setupOnScrollListener()

        pokemonCardListAdapter =
            PokemonCardListAdapter(imageLoader, ::cardToDetailFragmentTransaction)

        val gridSpacingItemDecoration = setupGridSpacingItemDecoration()

        val gridLayoutManager = GridLayoutManager(
            context,
            VERTICAL_SPAN_COUNT
        )
        binding.recyclerViewPokedexList.apply {
            layoutManager = gridLayoutManager
            adapter = pokemonCardListAdapter
            setHasFixedSize(true)
            addItemDecoration(gridSpacingItemDecoration)
            addOnScrollListener(scrollListener)
        }
    }

    private fun updateAdapterAndProgressBar(
        progressBarVisibility: Int,
        listToUpdate: List<PokemonCardInfo>,
    ) {
        pokemonCardListAdapter.submitList(listToUpdate)

        if (progressBarVisibility == View.GONE) {
            binding.progressBarBottom.visibility = progressBarVisibility
            binding.progressBarCenter.visibility = progressBarVisibility
            return
        }

        if (viewModel.pokemonCardInfoListState.value.pokemonCardList.isNotEmpty()) {
            binding.progressBarBottom.visibility = progressBarVisibility
        } else {
            binding.progressBarCenter.visibility = progressBarVisibility
        }
    }

    private fun setupOnScrollListener() = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (!recyclerView.canScrollVertically(1)    // can't scroll any further
                && newState == RecyclerView.SCROLL_STATE_IDLE
                && !viewModel.pokemonCardInfoListState.value.isLoading
            ) {
                binding.recyclerViewPokedexList.adapter?.itemCount?.let {
                    viewModel.fetchPokemonList(
                        offset = it,
                        isInitialFetch = false
                    )
                }
            }
        }
    }

    private fun setupGridSpacingItemDecoration(): GridSpacingItemDecoration {
        val spanCount = 2
        val verticalSpacing = resources
            .getDimension(R.dimen.pokemon_card_item_horizontal_margin_5dp)
            .toInt()
        val horizontalSpacing = resources
            .getDimension(R.dimen.pokemon_card_item_vertical_margin_5dp)
            .toInt()
        return GridSpacingItemDecoration(
            spanCount = spanCount,
            verticalSpacing = verticalSpacing,
            horizontalSpacing = horizontalSpacing
        )
    }

    private fun cardToDetailFragmentTransaction(pokemonCardInfo: PokemonCardInfo) {
        val pokemonDetailFragment = PokemonDetailFragment.newInstance(pokemonCardInfo)
        requireActivity().supportFragmentManager.commit {
            replace(R.id.fragment_container_view_main, pokemonDetailFragment)
            setReorderingAllowed(true)
            addToBackStack(id.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val VERTICAL_SPAN_COUNT = 2

        private const val TAG = "PokemonCardListFragment"
    }

}