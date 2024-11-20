package eone.grim.harrypoter.ui.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import eone.grim.harrypoter.R
import eone.grim.harrypoter.databinding.FragmentCharactersBinding

class CharactersFragment : Fragment() {

    private var _binding: FragmentCharactersBinding? = null
    private lateinit var characterAdapter: CharacterAdapter


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val charactersViewModel =
            ViewModelProvider(this).get(CharactersViewModel::class.java)

        _binding = FragmentCharactersBinding.inflate(inflater, container, false)
        val root: View = binding.root


        characterAdapter = CharacterAdapter{
            findNavController().navigate(CharactersFragmentDirections.actionNavigationCharactersToCharacterDetailed(it))
        } //TODO write click listener
        val rv = binding.charactersRv
        rv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = characterAdapter
        }

        charactersViewModel.characters.observe(viewLifecycleOwner, Observer { characters ->
            characters?.let {
                characterAdapter.setCharacters(it)
            }
        })


        binding.chipGroupHouses.setOnCheckedStateChangeListener { group, checkedIds ->

            val selectedHouses = checkedIds.map { id ->
                when (id) {
                    R.id.chip_griffindor -> "Gryffindor"
                    R.id.chip_slytherin -> "Slytherin"
                    R.id.chip_hufflepuff -> "Hufflepuff"
                    R.id.chip_ravenclaw -> "Ravenclaw"
                    R.id.chip_houseless -> ""
                    else -> null
                }
            }.filterNotNull()

            charactersViewModel.loadCharactersByHouses(selectedHouses)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}