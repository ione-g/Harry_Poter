package eone.grim.harrypoter.ui.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import eone.grim.harrypoter.R
import eone.grim.harrypoter.databinding.FragmentCharacterDetailedBinding
import eone.grim.harrypoter.entities.Character

class CharacterDetailed: Fragment() {


    private var _binding: FragmentCharacterDetailedBinding? = null

    private val HOUSES = listOf("Gryffindor", "Slytherin", "Ravenclaw", "Hufflepuff", "Houseless")


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacterDetailedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val character = arguments?.let {
            CharacterDetailedArgs.fromBundle(it).character
        }


        if (!character!!.wizard || !character.alive) { //only alive wizards can teach magic
            binding.trainSpell.visibility = View.GONE
        }
        val charactersViewModel = ViewModelProvider(this)[CharactersViewModel::class.java]
        charactersViewModel.characterSpells.observe(viewLifecycleOwner) {
            val arrayNames = mutableListOf<String>()
            it.forEach { spell -> arrayNames.add(spell.name!!)}
                if (arrayNames.size>0) {
                    binding.characterSpellListText.text = arrayNames.toString()
                }else
                    binding.characterSpellListLl.visibility = View.GONE
        }


        loadUI(character)
        setUpHousesDropdownMenu(character,binding.characterHouseSpinner,charactersViewModel)

        character.let {
            binding.character = it
        }
        val characterId = character.id
        val trainSpellButton= binding.trainSpell
        trainSpellButton.setOnClickListener{
            findNavController().navigate(CharacterDetailedDirections.actionCharacterDetailedToNavigationSpells(characterId))
        }
        charactersViewModel.getCharacterWithSpells(characterId)


        return root
    }

    private fun loadUI(character: Character) {
        if (character.image.isNullOrEmpty()) {
            binding.imageView.visibility = View.GONE
        } else {
            Glide.with(this)
                .load(character.image)
                .placeholder(R.drawable.character_image_placeholder)
//                .error(R.drawable.character_image_placeholder) //change to error
                .into(binding.imageView)
        }

        binding.characterIdText.text =  character.id
        binding.characterNameText.text =  character.name

        isFieldIsNullOrEmpty(character.alternateNames.toString(),binding.characterAlternateNamesLl,binding.characterAlternateNamesText)
        isFieldIsNullOrEmpty(character.species,binding.characterSpeciesLl,binding.characterSpeciesText)
        isFieldIsNullOrEmpty(character.gender,binding.characterGenderLl,binding.characterGenderText)
        isFieldIsNullOrEmpty(character.dateOfBirth,binding.characterDataOfBirthLl,binding.characterDataOfBirthText)
        isFieldIsNullOrEmpty(character.ancestry,binding.characterAncestryLl,binding.characterAncestryText)
        isFieldIsNullOrEmpty(character.eyeColour,binding.characterEyeColorLl,binding.characterEyeColorText)
        isFieldIsNullOrEmpty(character.hairColour,binding.characterHairColorLl,binding.characterHairColorText)
        isFieldIsNullOrEmpty(character.patronus,binding.characterPatronusLl,binding.characterPatronusText)

        isFieldIsNullOrEmpty(character.actor,binding.characterActorLl,binding.characterActorText)
        isFieldIsNullOrEmpty(character.alternateActors.toString(),binding.characterAlternativeActorsLl,binding.characterAlternativeActorsText)


        if (character.wizard) {
            binding.characterIsWizardText.text = getString(R.string.character_is_wizard)
        } else {
            binding.characterIsWizardText.text = getString(R.string.character_is_not_wizard)
        }

        if (character.yearOfBirth == null) {
            binding.characterYearOfBirthLl.visibility = View.GONE
        } else {
            binding.characterYearOfBirthText.text = "${character.yearOfBirth}"
        }

        if (character.wand == null) {
            binding.characterWandLl.visibility = View.GONE
        } else {
            binding.characterWandText.text = character.wand.toString()
        }

        if (character.alive) {
            binding.characterAliveText.text = getString(R.string.character_is_alive)
        } else {
            binding.characterAliveText.text = getString(R.string.character_is_dead)
        }

        if (character.hogwartsStaff) {
            binding.characterHogwartsStuffText.text = getString(R.string.working_in_hogwarts)
        } else {
            binding.characterHogwartsStuffLl.visibility = View.GONE
        }

        if (character.hogwartsStudent) {
            binding.characterHogwartsStudentText.text =
                getString(R.string.character_studied_at_hogwards)
        } else {
            binding.characterHogwartsStudentLl.visibility = View.GONE
        }

    }

    private fun setUpHousesDropdownMenu(
        character: Character,
        houseSpinner: Spinner,
        charactersViewModel: CharactersViewModel
    ) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, HOUSES)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        houseSpinner.adapter = adapter
        val characterHouse = when  (character.house) {
            "Gryffindor" ->"Gryffindor"
            "Slytherin" ->"Slytherin"
            "Ravenclaw" ->"Ravenclaw"
            "Hufflepuff" ->"Hufflepuff"
            else -> {"Houseless"}
        }

        val housePosition = HOUSES.indexOf(characterHouse)
        if (housePosition != -1) {
            houseSpinner.setSelection(housePosition)
        }

        houseSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?, selectedItemView: View?,
                position: Int, id: Long
            ) {
                val selectedHouse = HOUSES[position]
                if (character.house != selectedHouse) {
                    character.house = selectedHouse
                    charactersViewModel.updateCharacter(character                    )
                    Toast.makeText(requireContext(),"Updating...",Toast.LENGTH_LONG).show()
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do nothing when no item is selected
            }
        }
    }

    private fun isFieldIsNullOrEmpty(field: String?,linearLayout:LinearLayout,textView:TextView) {
        if (field.isNullOrEmpty())
            linearLayout.visibility = View.GONE
        else
            textView.text = field
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}