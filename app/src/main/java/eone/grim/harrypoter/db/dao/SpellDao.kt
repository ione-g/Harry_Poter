package eone.grim.harrypoter.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import eone.grim.harrypoter.entities.CharacterWithSpells
import eone.grim.harrypoter.entities.Spell
import eone.grim.harrypoter.entities.SpellWithCharacters

@Dao
interface SpellDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpells(spells: List<Spell>)

    @Query("SELECT * FROM spells")
    fun getAllSpells(): List<Spell>

    @Query("SELECT EXISTS(SELECT 1 FROM spells LIMIT 1)")
    suspend fun hasSpells(): Boolean


    @Transaction
    @Query("SELECT * FROM spells WHERE id = :spellId")
    suspend fun getSpellWithCharacters(spellId: String?): SpellWithCharacters
}