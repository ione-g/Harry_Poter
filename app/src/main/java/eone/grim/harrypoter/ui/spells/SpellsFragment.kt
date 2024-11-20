package eone.grim.harrypoter.ui.spells

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import eone.grim.harrypoter.databinding.FragmentSpellsBinding
import eone.grim.harrypoter.entities.CharacterSpellRef
import eone.grim.harrypoter.entities.Spell
import eone.grim.harrypoter.ui.characters.CharactersViewModel

class SpellsFragment : Fragment() {

    private var _binding: FragmentSpellsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val spellsViewModel =
            ViewModelProvider(this)[SpellsViewModel::class.java]
        val charactersViewModel =
            ViewModelProvider(this)[CharactersViewModel::class.java]

        _binding = FragmentSpellsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val characterId = arguments?.let {
            it.getString("characterId")
        }

        characterId?.let { Log.d("CHARACTER ID", it) }

        var onSpellClick: (Spell) -> Unit
        if (!characterId.isNullOrEmpty()) {
            onSpellClick = {spell->
                run {
                    charactersViewModel.createRef(CharacterSpellRef(characterId,spell.id))
                    findNavController().popBackStack()
                }
            }
        } else {
            onSpellClick = { spell ->
                findNavController().navigate(
                    SpellsFragmentDirections.actionNavigationSpellsToSpellDetailed(spell = spell)
                )
            }
        }
        val spellAdapter = SpellAdapter{
            onSpellClick(it)
        } //TODO write click listener
        val rv = binding.spellsRv
        rv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = spellAdapter
        }

        spellsViewModel.spellList.observe(viewLifecycleOwner,  { characters ->
            characters?.let {
                spellAdapter.setSpells(it)
            }
        })

        binding.trainWizardsRandomSpell.setOnClickListener{
           val taughtSpell = spellsViewModel.teachRandomSpellToAllCharacters()
            Toast.makeText(requireContext(),"All alive wizards taught ${taughtSpell?.name}",Toast.LENGTH_LONG).show()
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}