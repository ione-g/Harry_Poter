package eone.grim.harrypoter.ui.characters

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eone.grim.harrypoter.db.AppDatabase
import eone.grim.harrypoter.db.dao.CharacterDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import eone.grim.harrypoter.entities.Character
import eone.grim.harrypoter.entities.CharacterSpellRef
import eone.grim.harrypoter.entities.Spell

class CharactersViewModel(application: Application) : AndroidViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text



    private val characterDao: CharacterDao = AppDatabase.getInstance(application).characterDao()
    private val _characters = MutableLiveData<List<Character>>()

    val characters: LiveData<List<Character>> = _characters

    init {
        loadCharacters()
    }

    private fun loadCharacters() {
        CoroutineScope(Dispatchers.IO).launch {
            val charactersList = characterDao.getAllCharacters()
            _characters.postValue(charactersList)
        }
    }

    fun createRef(characterSpellRef: CharacterSpellRef) {
        viewModelScope.launch {
            characterDao.insertCharacterSpellRef(characterSpellRef)
        }
    }


    private val _characterSpells = MutableLiveData<List<Spell>>()

    val characterSpells: LiveData<List<Spell>> = _characterSpells

    fun getCharacterWithSpells(characterId: String?) {
        viewModelScope.launch {
            try {
                val characterWithSpells = characterDao.getCharacterWithSpells(characterId)
                _characterSpells.postValue(characterWithSpells.spells)
            } catch (e: Exception) {
                e.printStackTrace()
                _characterSpells.postValue(emptyList())
            }
        }
    }

    fun loadCharactersByHouses(houses: List<String>) {
        viewModelScope.launch {
            try {
                val filteredCharacters = characterDao.getCharactersByHouses(houses)
                _characters.postValue(filteredCharacters)
            } catch (e: Exception) {
                e.printStackTrace()
                _characters.postValue(emptyList())
            }
        }
    }
}

