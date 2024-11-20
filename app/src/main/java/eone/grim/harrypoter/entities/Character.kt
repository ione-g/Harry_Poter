package eone.grim.harrypoter.entities


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import eone.grim.harrypoter.db.Converters
import eone.grim.harrypoter.entities.Wand
import java.io.Serializable

@Entity(tableName = "characters")
@TypeConverters(Converters::class)
data class Character(

    @PrimaryKey val id: String,
    val name: String? = null,
    @SerializedName("alternate_names")
    val alternateNames: List<String>? = null,
    val species: String? = null,
    val gender: String? = null,
    var house: String? = null,
    val dateOfBirth: String? = null,
    val yearOfBirth: Int? = 0,
    val wizard: Boolean = false,
    val ancestry: String? = null,
    val eyeColour: String? = null,
    val hairColour: String? = null,
    val wand: Wand? = null,
    val patronus: String? = null,
    val hogwartsStudent: Boolean = false,
    val hogwartsStaff: Boolean = false,
    val actor: String? = null,
    @SerializedName("alternate_actors")
    val alternateActors: List<String>? = null,
    val alive: Boolean = false,
    val image: String? = null
):Serializable