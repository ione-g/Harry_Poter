package eone.grim.harrypoter.ui.spells

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eone.grim.harrypoter.db.AppDatabase
import eone.grim.harrypoter.db.dao.CharacterDao
import eone.grim.harrypoter.db.dao.SpellDao
import eone.grim.harrypoter.entities.Character
import eone.grim.harrypoter.entities.Spell
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class SpellsViewModel (application: Application) : AndroidViewModel(application)  {

    private val _text = MutableLiveData<String>().apply {
        value = "This is spells Fragment"
    }
    val text: LiveData<String> = _text



    private val spellDao: SpellDao = AppDatabase.getInstance(application).spellDao()
    private val characterDao: CharacterDao = AppDatabase.getInstance(application).characterDao()
    private val _spells= MutableLiveData<List<Spell>>()

    val spellList: LiveData<List<Spell>> = _spells

    init {
        loadCharacters()
    }

    private fun loadCharacters() {
        CoroutineScope(Dispatchers.IO).launch {
            val spellList = spellDao.getAllSpells()
            _spells.postValue(spellList)
        }
    }



    private val _spellCharacters = MutableLiveData<List<Character>>()

    val characterSpells: LiveData<List<Character>> = _spellCharacters

    fun getCharacterWithSpells(spellId: String?) {
        viewModelScope.launch {
            try {
                val spellWithCharacters = spellDao.getSpellWithCharacters(spellId)
                _spellCharacters.postValue(spellWithCharacters.characters)
            } catch (e: Exception) {
                e.printStackTrace()
                _spellCharacters.postValue(emptyList())
            }
        }
    }

    fun getRandomSpell(): Spell? {
        val spellList = _spells.value
        return spellList?.random()
    }

    fun teachRandomSpellToAllCharacters():Spell? {
        var randomSpell:Spell? = null
        viewModelScope.launch {
            randomSpell = getRandomSpell()
            randomSpell?.let {
                characterDao.teachSpellToAllCharacters(it)
            }
        }
        return randomSpell
    }

}