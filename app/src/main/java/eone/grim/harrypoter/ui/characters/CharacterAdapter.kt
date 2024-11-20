package eone.grim.harrypoter.ui.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eone.grim.harrypoter.databinding.CharacterItemBinding
import eone.grim.harrypoter.entities.Character

class CharacterAdapter(private val onCharacterClick: (Character) -> Unit) : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    private var characters: List<Character> = emptyList()

    fun setCharacters(characters: List<Character>) {
        this.characters = characters
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = CharacterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(binding, onCharacterClick)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = characters[position]
        holder.bind(character)
    }

    override fun getItemCount(): Int {
        return characters.size
    }

    class CharacterViewHolder(
        private val binding: CharacterItemBinding,
        private val onCharacterClick: (Character) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(character: Character) {
            binding.character = character
            binding.executePendingBindings()

            binding.root.setOnClickListener {
                onCharacterClick(character)
            }
        }
    }
}
