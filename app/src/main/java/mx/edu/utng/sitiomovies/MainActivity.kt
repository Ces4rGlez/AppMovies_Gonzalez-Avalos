package mx.edu.utng.sitiomovies

import android.os.Bundle
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.recyclerview.widget.GridLayoutManager
import android.content.Context
import android.content.SharedPreferences
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: MovieAdapter
    private var movieList = mutableListOf<Movie>()
    private lateinit var sharedPreferences: SharedPreferences

    // ActivityResultLauncher para AddActivity
    private val addMovieLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            loadMovies() // Recargar las películas
        }
    }

    // ActivityResultLauncher para EditActivity
    private val editMovieLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            loadMovies() // Recargar las películas
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)

        val isDarkMode = sharedPreferences.getBoolean("DarkMode", false)
        val isHighContrast = sharedPreferences.getBoolean("HighContrast", false)

        when {
            isHighContrast -> setTheme(R.style.Theme_SitioMovies_HighContrast)
            isDarkMode -> setTheme(R.style.Theme_SitioMovies_Dark)
            else -> setTheme(R.style.Theme_SitioMovies)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Debe ir primero
        dbHelper = DatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerView)

        // Cambiar a GridLayoutManager para 2 columnas
        recyclerView.layoutManager = GridLayoutManager(this, 2) // El segundo parámetro es el número de columnas

        // Configurar el adaptador
        movieAdapter = MovieAdapter(movieList) { movie ->
            // Abrir la pantalla de edición
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("MOVIE", movie)
            }
            editMovieLauncher.launch(intent)
        }
        recyclerView.adapter = movieAdapter

        // Cargar las películas
        loadMovies()

        loadRecommendedMovies()

        // Configurar la búsqueda en tiempo real
        val searchView = findViewById<androidx.appcompat.widget.SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    loadMovies() // Mostrar todas las películas si no hay texto
                } else {
                    filterMovies(newText) // Filtrar películas
                }
                return true
            }
        })

        // Configurar FloatingActionButton para agregar una nueva película
        val fabAdd: FloatingActionButton = findViewById(R.id.fabAdd)
        fabAdd.setOnClickListener {
            // Crear un Intent para redirigir a AddActivity
            val intent = Intent(this, AddActivity::class.java)
            addMovieLauncher.launch(intent) // Usar addMovieLauncher
        }
        val settingsButton: FloatingActionButton = findViewById(R.id.btnSettings)
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

    }
    private fun loadRecommendedMovies() {
        // Obtener películas con rating mayor a 3.0 para mostrar las mejor valoradas
        val recommendedMovies = dbHelper.getRecommendedMovies("", 3.0f)

        // Configurar el RecyclerView
        val recyclerViewRecommended = findViewById<RecyclerView>(R.id.recyclerViewRecommended)
        recyclerViewRecommended.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val recommendedAdapter = MovieAdapter(recommendedMovies) { movie ->
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("MOVIE", movie)
            }
            startActivity(intent)
        }
        recyclerViewRecommended.adapter = recommendedAdapter
    }


    private fun loadMovies() {
        movieList.clear()
        movieList.addAll(dbHelper.getAllMovies())
        movieAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        loadMovies() // Recargar las películas al volver a la actividad
        loadRecommendedMovies() // Agregar esta línea
    }

    private fun filterMovies(query: String) {
        val filteredList = movieList.filter { movie ->
            movie.title.contains(query, ignoreCase = true) ||
                    movie.genre.contains(query, ignoreCase = true) ||
                    movie.director.contains(query, ignoreCase = true)
        }
        movieAdapter = MovieAdapter(filteredList.toMutableList()) { movie ->
            val intent = Intent(this, EditActivity::class.java).apply {
                putExtra("MOVIE", movie)
            }
            editMovieLauncher.launch(intent)
        }
        recyclerView.adapter = movieAdapter
    }
}