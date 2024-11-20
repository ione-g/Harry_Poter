package eone.grim.harrypoter.ui.spells

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eone.grim.harrypoter.databinding.SpellItemBinding
import eone.grim.harrypoter.entities.Spell

class SpellAdapter(private val onSpellClick: (Spell) -> Unit) : RecyclerView.Adapter<SpellAdapter.SpellViewHolder>()  {

    private var spells: List<Spell> = emptyList()

    fun setSpells(spells: List<Spell>) {
        this.spells = spells
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpellViewHolder {
        val binding = SpellItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SpellViewHolder(binding, onSpellClick)
    }

    override fun onBindViewHolder(holder: SpellViewHolder, position: Int) {
        val spell = spells[position]
        holder.bind(spell)
    }

    override fun getItemCount(): Int {
        return spells.size
    }

    class SpellViewHolder(
        private val binding: SpellItemBinding,
        private val onSpellClick: (Spell) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(spell: Spell) {
            binding.spell = spell
            binding.executePendingBindings()

            binding.root.setOnClickListener {
                onSpellClick(spell)
            }
        }
    }

}