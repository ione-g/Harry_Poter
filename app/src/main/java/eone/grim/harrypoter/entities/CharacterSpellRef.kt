package eone.grim.harrypoter.entities

import androidx.room.Entity

@Entity(primaryKeys = ["characterId", "spellId"],
    )
data class CharacterSpellRef(
    val characterId: String,
    val spellId: String
)
