package eone.grim.harrypoter.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "spells")
data class Spell (
    @PrimaryKey val id: String,
    val name: String? = null,
    val description: String? = null
):Serializable
