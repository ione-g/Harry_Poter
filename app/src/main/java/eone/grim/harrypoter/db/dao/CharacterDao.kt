package eone.grim.harrypoter.db.dao

import eone.grim.harrypoter.entities.Character
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import eone.grim.harrypoter.entities.CharacterSpellRef
import eone.grim.harrypoter.entities.CharacterWithSpells
import eone.grim.harrypoter.entities.Spell

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<Character>)

    @Update
    suspend fun updateCharacter(character: Character)

    @Query("SELECT * FROM characters")
    fun getAllCharacters(): List<Character>

    @Query("SELECT * FROM characters WHERE alive = 1 and wizard = 1")
    fun getAliveWizards(): List<Character>

    @Query("SELECT EXISTS(SELECT 1 FROM characters LIMIT 1)")
    suspend fun hasCharacters(): Boolean


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacterSpellRef(ref: CharacterSpellRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCharacterSpellRefs(refs: List<CharacterSpellRef>)

    @Transaction
    suspend fun teachSpellToAllCharacters(spell: Spell) {
        val charactersList = getAliveWizards()
        val characterSpellRefs = charactersList.map { CharacterSpellRef(characterId = it.id, spellId = spell.id) }
        insertAllCharacterSpellRefs(characterSpellRefs)
    }

    @Query("SELECT * FROM characters WHERE house IN (:houses)")
    suspend fun getCharactersByHouses(houses: List<String>): List<Character>


    @Transaction
    @Query("SELECT * FROM characters WHERE id = :characterId")
    suspend fun getCharacterWithSpells(characterId: String?):CharacterWithSpells //A list of Spells associate with Character
}