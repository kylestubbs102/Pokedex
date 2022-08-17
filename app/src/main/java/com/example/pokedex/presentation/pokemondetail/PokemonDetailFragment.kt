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
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.pokedex.R
import com.example.pokedex.databinding.FragmentPokemonDetailBinding
import com.example.pokedex.domain.interfaces.ImageLoader
import com.example.pokedex.domain.model.PokemonAboutInfo
import com.example.pokedex.domain.model.PokemonInfo
import com.example.pokedex.util.Helpers
import com.example.pokedex.util.PokemonColorUtils
import com.example.pokedex.util.Resource
import com.google.android.material.appbar.AppBarLayout
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

    private lateinit var _pokemonInfo: PokemonInfo

    private val imageLoader: ImageLoader by inject()

    private val appBarLayoutListener =
        AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val newImageAndIdAlpha =
                (1.0f - abs(verticalOffset / appBarLayout.totalScrollRange.toFloat()))
            binding.imageViewPokemonDetail.alpha = newImageAndIdAlpha
            binding.textViewPokemonId.alpha = newImageAndIdAlpha
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel
                        .pokemonAboutInfoFlow
                        .collect { pokemonAboutInfo ->
                            when (pokemonAboutInfo) {
                                is Resource.Loading -> {
                                    binding.progressBarPokemonDetail.visibility = View.VISIBLE
                                }
                                is Resource.Success -> {
                                    binding.progressBarPokemonDetail.visibility = View.GONE
                                    activity?.invalidateOptionsMenu()
                                    pokemonAboutInfo.data?.let { setupGenus(it) }
                                }
                                is Resource.Error -> {
                                    showErrorScreen()
                                    binding.progressBarPokemonDetail.visibility = View.GONE
                                }
                            }
                        }
                }
                launch {
                    viewModel
                        .pokemonLikedFlow
                        .collect { isLiked ->
                            _pokemonInfo = _pokemonInfo.copy(isLiked = isLiked)
                        }
                }
            }
        }

        setupMenuProvider()

        val pokemonInfo: PokemonInfo? =
            arguments?.getParcelable(POKEMON_CARD_INFO_ARGUMENT_KEY)

        if (pokemonInfo != null) {
            _pokemonInfo = pokemonInfo
            viewModel.getPokemonAboutInfo(pokemonInfo.id)
            viewModel.getPokemonLikedValue(pokemonInfo.id)

            setupPokemonViews(pokemonInfo)
            setupViewPager(pokemonInfo.id)
            setupToolbar()
        } else {
            showErrorScreen()
        }

    }

    private fun setupGenus(pokemonAboutInfo: PokemonAboutInfo) {
        binding.apply {
            textViewGenus.text = pokemonAboutInfo.genus
        }
    }

    private fun setupMenuProvider() {
        val menuHost: MenuHost = requireActivity()

        val menuProvider = object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                super.onPrepareMenu(menu)
                val likedIconTintColor =
                    PokemonColorUtils.getPokemonIconLikedTintColor(_pokemonInfo)

                setDrawableTint(
                    DrawableCompat.wrap(menu.findItem(R.id.menu_item_liked).icon),
                    likedIconTintColor
                )
                setIconsVisibility(_pokemonInfo.isLiked)
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                binding.toolbarPokemonDetail.inflateMenu(R.menu.pokemon_detail_menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_item_liked -> {
                        _pokemonInfo = _pokemonInfo.copy(isLiked = false)
                        activity?.invalidateOptionsMenu()
                        viewModel.updateLikedValue(_pokemonInfo.id, false)
                        false
                    }
                    R.id.menu_item_not_liked -> {
                        _pokemonInfo = _pokemonInfo.copy(isLiked = true)
                        activity?.invalidateOptionsMenu()
                        viewModel.updateLikedValue(_pokemonInfo.id, true)
                        false
                    }
                    android.R.id.home -> {
                        parentFragmentManager.popBackStack()
                        false
                    }
                    else -> true
                }
            }

        }

        menuHost.addMenuProvider(menuProvider, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupToolbar() {
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

    private fun setupPokemonViews(pokemonInfo: PokemonInfo) {
        binding.apply {
            appBarLayoutPokemonDetail.setBackgroundColor(
                ContextCompat.getColor(
                    root.context,
                    PokemonColorUtils.getPokemonColor(pokemonInfo)
                )
            )
            collapsingToolbarLayoutPokemonDetail.apply {
                title = pokemonInfo.name.replaceFirstChar {
                    it.uppercase()
                }
            }
            textViewPokemonId.apply {
                text = pokemonInfo
                    .id
                    .toString()
                    .padStart(3, '0')
                    .prependIndent("#")
            }

            imageLoader.loadImage(
                url = Helpers.getImageUrl(pokemonInfo.id),
                imageView = imageViewPokemonDetail
            )

            appBarLayoutPokemonDetail.addOnOffsetChangedListener(appBarLayoutListener)
        }

        setupTypes(pokemonInfo)
    }

    private fun setupTypes(pokemonInfo: PokemonInfo) {
        binding.apply {
            when (pokemonInfo.types.size) {
                0 -> {
                    textViewType1.visibility = View.INVISIBLE
                    textViewType2.visibility = View.INVISIBLE
                }
                1 -> {
                    textViewType1.text = pokemonInfo.types[0].replaceFirstChar { it.uppercase() }
                    textViewType2.visibility = View.INVISIBLE
                }
                2 -> {
                    textViewType1.text = pokemonInfo.types[0].replaceFirstChar { it.uppercase() }
                    textViewType2.text = pokemonInfo.types[1].replaceFirstChar { it.uppercase() }
                }
                else -> throw IndexOutOfBoundsException("PokemonInfo contains more than two types.")
            }
        }
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

    private fun setIconsVisibility(isLiked: Boolean) {
        binding.toolbarPokemonDetail.menu.apply {
            findItem(R.id.menu_item_liked).isVisible = isLiked
            findItem(R.id.menu_item_not_liked).isVisible = !isLiked
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.appBarLayoutPokemonDetail.removeOnOffsetChangedListener(appBarLayoutListener)
        _binding = null
    }

    companion object {
        fun newInstance(pokemonInfo: PokemonInfo): PokemonDetailFragment {
            val pokemonDetailFragment = PokemonDetailFragment()
            val args = Bundle().apply {
                putParcelable(POKEMON_CARD_INFO_ARGUMENT_KEY, pokemonInfo)
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
