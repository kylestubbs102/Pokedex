package com.example.pokedex.presentation.pokemondetail.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.pokedex.databinding.FragmentPokemonDetailAboutBinding
import com.example.pokedex.domain.model.PokemonInfo
import com.example.pokedex.presentation.pokemondetail.PokemonDetailViewModel
import com.example.pokedex.util.Resource
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel

class PokemonDetailAboutFragment : Fragment() {

    private val viewModel by lazy {
        requireParentFragment().getViewModel<PokemonDetailViewModel>()
    }

    private var _binding: FragmentPokemonDetailAboutBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonDetailAboutBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pokemonInfoFlow
                    .collect { pokemonInfoState ->
                        when (pokemonInfoState) {
                            is Resource.Loading -> {
                                // TODO : maybe use shimmer
                            }
                            is Resource.Success -> {
                                pokemonInfoState.data?.let { bindPokemonInfo(it) }
                            }
                            is Resource.Error -> {
                                // TODO : error handled in parent fragment
                            }
                        }
                    }
            }
        }
    }


    private fun bindPokemonInfo(pokemonInfo: PokemonInfo) {
        setDescription(pokemonInfo)
        setHeightAndWeight(pokemonInfo)
        setGenderRates(pokemonInfo.genderRate)
        setEggGroupsAndCycles(pokemonInfo)
        setBaseExperience(pokemonInfo)
    }

    private fun setDescription(pokemonInfo: PokemonInfo) {
        binding.apply {
            textViewDescription.text = pokemonInfo.description
        }
    }

    private fun setHeightAndWeight(pokemonInfo: PokemonInfo) {
        binding.apply {
            textViewHeight.text = getHeightText(pokemonInfo.height)
            textViewWeight.text = getWeightText(pokemonInfo.weight)
        }
    }

    private fun getHeightText(height: Int): String {
        val heightInInches = height / .254F
        val feet = (heightInInches / 12).toInt()
        val inches = heightInInches % 12F
        val heightInMeters = height / 10F

        return buildString {
            append("$feet'${"%.1f".format(inches)}\"")
            append(" ")
            append("(${"%.2f".format(heightInMeters)} cm)")
        }
    }

    private fun getWeightText(weight: Int): String {
        val weightInLbs = weight / 4.536F
        val weightInKg = weight / 10F

        return buildString {
            append("${"%.1f".format(weightInLbs)} lbs")
            append(" ")
            append("(${"%.1f".format(weightInKg)} kg)")
        }
    }

    private fun setGenderRates(genderRate: Int) {
        binding.apply {
            if (genderRate == -1) {    // Genderless
                textViewGenderFemale.visibility = View.GONE
                textViewGenderMale.visibility = View.GONE
                textViewGenderless.visibility = View.VISIBLE
                return@apply
            }

            textViewGenderFemale.visibility = View.VISIBLE
            textViewGenderMale.visibility = View.VISIBLE
            textViewGenderless.visibility = View.GONE

            val genderDecimal = (genderRate / 8F)
            val maleGenderPercent = ((1 - genderDecimal) * 100)
            val femaleGenderPercent = (genderDecimal * 100)
            if (maleGenderPercent == 0F) {
                textViewGenderMale.visibility = View.GONE
            } else {
                textViewGenderMale.text = "$maleGenderPercent%"
            }
            if (femaleGenderPercent == 0F) {
                textViewGenderFemale.visibility = View.GONE
            } else {
                textViewGenderFemale.text = "$femaleGenderPercent%"
            }
        }
    }

    private fun setEggGroupsAndCycles(pokemonInfo: PokemonInfo) {
        binding.apply {
            textViewEggGroups.text = pokemonInfo.eggGroups.joinToString(", ") { group ->
                group.replaceFirstChar { it.uppercase() }
            }
            textViewEggCycles.text = pokemonInfo.eggCycles.toString()
        }
    }

    private fun setBaseExperience(pokemonInfo: PokemonInfo) {
        binding.apply {
            textViewBaseExperience.text = pokemonInfo.baseExperience.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PokemonDetailAboutFragment()
    }
}