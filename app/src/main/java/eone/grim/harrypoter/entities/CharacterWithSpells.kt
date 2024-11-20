package eone.grim.harrypoter.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class CharacterWithSpells(
    @Embedded val character: Character,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            CharacterSpellRef::class,
            parentColumn = "characterId",
            entityColumn = "spellId" )
    )
    val spells: List<Spell>
)