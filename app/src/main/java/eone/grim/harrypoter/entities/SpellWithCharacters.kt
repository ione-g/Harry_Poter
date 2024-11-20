package eone.grim.harrypoter.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class SpellWithCharacters(
    @Embedded val spell: Spell,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            CharacterSpellRef::class,
            parentColumn = "spellId",
            entityColumn = "characterId" )
    )
    val characters: List<Character>
)