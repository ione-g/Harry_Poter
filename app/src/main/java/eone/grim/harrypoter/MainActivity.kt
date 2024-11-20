package eone.grim.harrypoter

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import eone.grim.harrypoter.api.ApiService
import eone.grim.harrypoter.databinding.ActivityMainBinding
import eone.grim.harrypoter.db.AppDatabase
import eone.grim.harrypoter.ui.characters.CharactersFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var rdb : AppDatabase

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
//                R.id.navigation_home,
                R.id.navigation_characters, R.id.navigation_spells
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        rdb = AppDatabase.getInstance(application)

        downloadInitialInfo()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


    private fun downloadInitialInfo() {
        fetchCharacters()
        fetchSpells()
    }




    private fun fetchSpells() {
        CoroutineScope(Dispatchers.IO).launch {
            try {

                if (rdb.spellDao().hasSpells()) {
                    Log.d("CHECKING", "Database already initialized. Skipping data download.")
                    return@launch
                }

                Log.d("CHECKING", "Database is empty. Fetching data from API.")

                val retrofit = Retrofit.Builder()
                    .baseUrl("https://hp-api.onrender.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val apiService = retrofit.create(ApiService::class.java)

                val spells = apiService.getSpells()
                if (spells.isNotEmpty()) {
                    rdb.spellDao().insertSpells(spells)
                    Log.d("CHECKING", "Inserted ${spells.size} spells into the database.")
                } else {
                    Log.e("API_ERROR", "Failed to fetch data")
                }
            } catch (e: Exception) {
                Log.e("EXCEPTION", "Error fetching or saving data: ${e.message}")
                e.printStackTrace()
            }

        }

    }

    private fun fetchCharacters() {
        CoroutineScope(Dispatchers.IO).launch {
            try {

                if (rdb.characterDao().hasCharacters()) {
                    Log.d("CHECKING", "Database already initialized. Skipping data download.")
                    return@launch
                }

                // Fetch data if the database is empty
                Log.d("CHECKING", "Database is empty. Fetching data from API.")

                val retrofit = Retrofit.Builder()
                    .baseUrl("https://hp-api.onrender.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val apiService = retrofit.create(ApiService::class.java)

                val characters = apiService.getCharacters()
                if (characters.isNotEmpty()) {
                    rdb.characterDao().insertCharacters(characters)
                    Log.d("CHECKING", "Inserted ${characters.size} characters into the database.")
                } else {
                    Log.e("API_ERROR", "Failed to fetch data")
                }
            } catch (e: Exception) {
                Log.e("EXCEPTION", "Error fetching or saving data: ${e.message}")
                e.printStackTrace()
            }

        }
    }
}