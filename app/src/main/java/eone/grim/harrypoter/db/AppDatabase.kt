package eone.grim.harrypoter.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import eone.grim.harrypoter.entities.Character
import eone.grim.harrypoter.entities.Spell
import eone.grim.harrypoter.db.dao.CharacterDao
import eone.grim.harrypoter.db.dao.SpellDao
import eone.grim.harrypoter.entities.CharacterSpellRef

@Database(entities = [Character::class,Spell::class,CharacterSpellRef::class], version = 8, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun spellDao(): SpellDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "hp_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}