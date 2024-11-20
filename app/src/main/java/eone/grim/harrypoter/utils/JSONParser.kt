package eone.grim.harrypoter.utils

import eone.grim.harrypoter.entities.Wand
import eone.grim.harrypoter.entities.Character
import org.json.JSONArray

class JSONParser {
    companion object {
        fun parseCharacters(json: String): List<Character> {
            val jsonArray = JSONArray(json)
            val characters = mutableListOf<Character>()

            for (i in 0 until jsonArray.length()) {
                val characterJson = jsonArray.getJSONObject(i)
                val wandJson = characterJson.optJSONObject("wand")

                val wand = wandJson?.let {
                    Wand(
                        wood = it.optString("wood", ""),
                        core = it.optString("core", ""),
                        length = it.optDouble("length", 0.0)
                    )
                }

                val character = Character(
                    id = characterJson.getString("id"),
                    name = characterJson.getString("name"),
                    alternateNames = characterJson.getJSONArray("alternate_names")?.toList()
                        ?: emptyList(),
                    species = characterJson.getString("species"),
                    gender = characterJson.getString("gender"),
                    house = characterJson.optString("house", null),
                    dateOfBirth = characterJson.optString("dateOfBirth", null),
                    yearOfBirth = characterJson.optInt("yearOfBirth", 0).takeIf { it != 0 },
                    wizard = characterJson.getBoolean("wizard"),
                    ancestry = characterJson.optString("ancestry", null),
                    eyeColour = characterJson.optString("eyeColour", null),
                    hairColour = characterJson.optString("hairColour", null),
                    wand = wand,
                    patronus = characterJson.optString("patronus", null),
                    hogwartsStudent = characterJson.getBoolean("hogwartsStudent"),
                    hogwartsStaff = characterJson.getBoolean("hogwartsStaff"),
                    actor = characterJson.getString("actor"),
                    alternateActors = characterJson.optJSONArray("alternate_actors")?.toList()
                        ?: emptyList(),
                    alive = characterJson.getBoolean("alive"),
                    image = characterJson.getString("image")
                )

                characters.add(character)

            }
            return characters
        }

        private fun JSONArray.toList(): List<String> {
            val list = mutableListOf<String>()
            for (i in 0 until this.length()) {
                list.add(this.getString(i))
            }
            return list
        }
    }


}