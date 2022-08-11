package com.example.pokedex.presentation.pokemondetail

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.pokedex.R
import com.example.pokedex.databinding.FragmentPokemonDetailBinding
import com.example.pokedex.domain.interfaces.ImageLoader
import com.example.pokedex.domain.model.PokemonCardInfo
import com.example.pokedex.util.PokemonColorUtils
import com.example.pokedex.util.Resource
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.math.abs
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PokemonDetailFragment : Fragment() {

    private val viewModel: PokemonDetailViewModel by viewModel()

    private var _binding: FragmentPokemonDetailBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var _pokemonCardInfo: PokemonCardInfo

    private var isLiked = false

    private val imageLoader: ImageLoader by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonDetailBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pokemonInfoFlow
                    .collect { uiState ->
                        when (uiState) {
                            is Resource.Loading -> {
                                binding.progressBarPokemonDetail.visibility = View.VISIBLE
                            }
                            is Resource.Success -> {
                                binding.progressBarPokemonDetail.visibility = View.GONE
                                uiState.data?.let { isLiked = it.isLiked }
                                activity?.invalidateOptionsMenu()
                            }
                            is Resource.Error -> {
                                showErrorScreen()
                                binding.progressBarPokemonDetail.visibility = View.GONE
                            }
                        }
                    }
            }
        }

        val pokemonCardInfo: PokemonCardInfo? =
            arguments?.getParcelable(POKEMON_CARD_INFO_ARGUMENT_KEY)

        if (pokemonCardInfo != null) {
            _pokemonCardInfo = pokemonCardInfo
            viewModel.fetchPokemonInfo(pokemonCardInfo.id)

            setupPokemonViews(pokemonCardInfo)
            setupViewPager(pokemonCardInfo.id)
            setupToolbar()
        } else {
            showErrorScreen()
        }

    }

    private fun setupToolbar() {
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarPokemonDetail)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setupViewPager(id: Int) {
        binding.viewPagerPokemonDetail.adapter = PokemonDetailFragmentPagerAdapter(this, id)
        TabLayoutMediator(
            binding.tabLayoutPokemonDetail,
            binding.viewPagerPokemonDetail
        ) { tab, position ->
            tab.text = when (position) {
                PokemonDetailFragmentPagerAdapter.ABOUT_POSITION -> TAB_TEXT_ABOUT
                PokemonDetailFragmentPagerAdapter.BASE_STATS_POSITION -> TAB_TEXT_BASE_STATS
                PokemonDetailFragmentPagerAdapter.EVOLUTION_POSITION -> TAB_TEXT_EVOLUTION
                PokemonDetailFragmentPagerAdapter.MOVES_POSITION -> TAB_TEXT_MOVES
                else -> throw IndexOutOfBoundsException("View pager has invalid position.")
            }
        }.attach()
    }

    private fun showErrorScreen() {
        // TODO : handle error case with dialog where pokemon can't be ready, maybe add message param
    }

    private fun setupPokemonViews(pokemonCardInfo: PokemonCardInfo) {
        val color = pokemonCardInfo.color
        binding.apply {
            appBarLayoutPokemonDetail.setBackgroundColor(
                ContextCompat.getColor(
                    root.context,
                    PokemonColorUtils.getPokemonColor(color)
                )
            )
            val textColor = ContextCompat.getColor(
                root.context,
                PokemonColorUtils.getPokemonTextColor(color)
            )
            toolbarPokemonId.setTextColor(
                ContextCompat.getColor(
                    root.context,
                    PokemonColorUtils.getPokemonTextColor(color)
                )
            )
            collapsingToolbarLayoutPokemonDetail.apply {
                title = pokemonCardInfo.name.replaceFirstChar {
                    it.uppercase()
                }
                setCollapsedTitleTextColor(textColor)
                setExpandedTitleColor(textColor)
            }
            toolbarPokemonId.apply {
                text = pokemonCardInfo
                    .id
                    .toString()
                    .padStart(3, '0')
                    .prependIndent("#")
            }

            // TODO : extract into functions and set loading placeholder
            imageLoader.loadImage(
                url = pokemonCardInfo.imageUrl,
                imageView = imageViewPokemonDetail
            )
        }

        binding.appBarLayoutPokemonDetail.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val newImageAndIdAlpha =
                (1.0f - abs(verticalOffset / appBarLayout.totalScrollRange.toFloat()))
            binding.imageViewPokemonDetail.alpha = newImageAndIdAlpha
            binding.toolbarPokemonId.alpha = newImageAndIdAlpha
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        val pokemonCardInfo: PokemonCardInfo? =
            arguments?.getParcelable(POKEMON_CARD_INFO_ARGUMENT_KEY)

        val iconTintColor = pokemonCardInfo?.let {
            PokemonColorUtils.getPokemonIconTintColor(
                it.color
            )
        } ?: R.color.white

        val likedIconTintColor = pokemonCardInfo?.let {
            PokemonColorUtils.getPokemonIconLikedTintColor(
                it.color
            )
        } ?: R.color.red_500

        setDrawableTint(
            DrawableCompat.wrap(menu.findItem(R.id.menu_item_not_liked).icon),
            iconTintColor
        )
        setDrawableTint(
            DrawableCompat.wrap(menu.findItem(R.id.menu_item_liked).icon),
            likedIconTintColor
        )
        binding.toolbarPokemonDetail.navigationIcon?.let { DrawableCompat.wrap(it) }?.let {
            setDrawableTint(
                it,
                iconTintColor
            )
        }
        setIconsVisibility(isLiked)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        binding.toolbarPokemonDetail.inflateMenu(R.menu.pokemon_detail_menu)
    }

    private fun setDrawableTint(
        drawable: Drawable,
        tintColor: Int
    ) {
        DrawableCompat.setTint(
            drawable,
            ContextCompat.getColor(requireContext(), tintColor)
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_liked -> {
                isLiked = false
                activity?.invalidateOptionsMenu()
                viewModel.updateLikedValue(_pokemonCardInfo.id, false)
            }
            R.id.menu_item_not_liked -> {
                isLiked = true
                activity?.invalidateOptionsMenu()
                viewModel.updateLikedValue(_pokemonCardInfo.id, true)
            }
            android.R.id.home -> {
                parentFragmentManager.popBackStack()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setIconsVisibility(isLiked: Boolean) {
        binding.toolbarPokemonDetail.menu.apply {
            findItem(R.id.menu_item_liked).isVisible = isLiked
            findItem(R.id.menu_item_not_liked).isVisible = !isLiked
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(pokemonCardInfo: PokemonCardInfo): PokemonDetailFragment {
            val pokemonDetailFragment = PokemonDetailFragment()
            val args = Bundle().apply {
                putParcelable(POKEMON_CARD_INFO_ARGUMENT_KEY, pokemonCardInfo)
            }
            pokemonDetailFragment.arguments = args
            return pokemonDetailFragment
        }

        private const val POKEMON_CARD_INFO_ARGUMENT_KEY = "pokemon_card_info_argument_key"

        private const val TAB_TEXT_ABOUT = "About"
        private const val TAB_TEXT_BASE_STATS = "Base Stats"
        private const val TAB_TEXT_EVOLUTION = "Evolution"
        private const val TAB_TEXT_MOVES = "Moves"
    }

}
