package eone.grim.harrypoter.db

import androidx.room.TypeConverter
import eone.grim.harrypoter.entities.Wand
import org.json.JSONArray
import org.json.JSONObject

class Converters {

    // Convert a list of Strings to a JSON array string for Room storage
    @TypeConverter
    fun fromList(list: List<String>?): String? {
        return list?.let { JSONArray(it).toString() }
    }

    // Convert a JSON array string back into a list of Strings
    @TypeConverter
    fun toList(data: String?): List<String>? {
        val jsonArray = data?.let { JSONArray(it) }
        val list = mutableListOf<String>()
        jsonArray?.let {
            for (i in 0 until it.length()) {
                list.add(it.getString(i))
            }
        }
        return list
    }

    // Convert the Wand object to a JSON string for Room storage
    @TypeConverter
    fun fromWand(wand: Wand?): String? {
        return wand?.let {
            val jsonObject = JSONObject()
            jsonObject.put("wood", it.wood)
            jsonObject.put("core", it.core)
            jsonObject.put("length", it.length)
            jsonObject.toString()
        }
    }

    // Convert a JSON string back into a Wand object
    @TypeConverter
    fun toWand(data: String?): Wand? {
        return data?.let {
            val jsonObject = JSONObject(it)
            Wand(
                wood = jsonObject.getString("wood"),
                core = jsonObject.getString("core"),
                length = jsonObject.getDouble("length")
            )
        }
    }

}
