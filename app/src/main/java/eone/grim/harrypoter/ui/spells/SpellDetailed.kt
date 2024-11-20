package eone.grim.harrypoter.ui.spells

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import eone.grim.harrypoter.databinding.FragmentSpellDetailedBinding
import eone.grim.harrypoter.databinding.SpellItemBinding
import eone.grim.harrypoter.entities.Spell

class SpellDetailed: Fragment() {

    private var _binding: FragmentSpellDetailedBinding? = null


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpellDetailedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val spell = arguments?.let {
            SpellDetailedArgs.fromBundle(it).spell
        }
        spell?.id?.let { Log.d("CHECKING", it) }

        spell?.let {
            binding.spell = it
        }

        val spellsViewModel = ViewModelProvider(this)[SpellsViewModel::class.java]
        spellsViewModel.characterSpells.observe(viewLifecycleOwner) {
            val arrayNames = mutableListOf<String>()
            it.forEach({character -> arrayNames.add(character.name!!) })
            binding.spellCharacterList.text = arrayNames.toString()
        }
        spellsViewModel.getCharacterWithSpells(spell!!.id)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}