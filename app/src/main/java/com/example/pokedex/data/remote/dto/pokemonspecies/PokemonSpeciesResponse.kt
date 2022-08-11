package com.example.pokedex.data.remote.dto.pokemonspecies


import com.fasterxml.jackson.annotation.JsonProperty

data class PokemonSpeciesResponse(
    @JsonProperty("base_happiness")
    val baseHappiness: Int,
    @JsonProperty("capture_rate")
    val captureRate: Int,
    @JsonProperty("color")
    val color: Color,
    @JsonProperty("egg_groups")
    val eggGroups: List<EggGroup>,
    @JsonProperty("evolution_chain")
    val evolutionChain: EvolutionChain?,
    @JsonProperty("evolves_from_species")
    val evolvesFromSpecies: EvolveFromSpecies?,
    @JsonProperty("flavor_text_entries")
    val flavorTextEntries: List<FlavorTextEntry>,
    @JsonProperty("form_descriptions")
    val formDescriptions: List<Any>,
    @JsonProperty("forms_switchable")
    val formsSwitchable: Boolean,
    @JsonProperty("gender_rate")
    val genderRate: Int,
    @JsonProperty("genera")
    val genera: List<Genera>,
    @JsonProperty("generation")
    val generation: Generation,
    @JsonProperty("growth_rate")
    val growthRate: GrowthRate,
    @JsonProperty("habitat")
    val habitat: Habitat?,
    @JsonProperty("has_gender_differences")
    val hasGenderDifferences: Boolean,
    @JsonProperty("hatch_counter")
    val hatchCounter: Int,
    @JsonProperty("id")
    val id: Int,
    @JsonProperty("is_baby")
    val isBaby: Boolean,
    @JsonProperty("is_legendary")
    val isLegendary: Boolean,
    @JsonProperty("is_mythical")
    val isMythical: Boolean,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("names")
    val names: List<Name>,
    @JsonProperty("order")
    val order: Int,
    @JsonProperty("pal_park_encounters")
    val palParkEncounters: List<PalParkEncounter>,
    @JsonProperty("pokedex_numbers")
    val pokedexNumbers: List<PokedexNumber>,
    @JsonProperty("shape")
    val shape: Shape?,
    @JsonProperty("varieties")
    val varieties: List<Variety>
)