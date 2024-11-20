package eone.grim.harrypoter.api

import retrofit2.http.GET
import eone.grim.harrypoter.entities.Character
import eone.grim.harrypoter.entities.Spell

interface ApiService {
    @GET("api/characters")
    suspend fun getCharacters(): List<Character>

    @GET("api/spells")
    suspend fun getSpells(): List<Spell>

}