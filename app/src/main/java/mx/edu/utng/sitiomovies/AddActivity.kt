package mx.edu.utng.sitiomovies

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        dbHelper = DatabaseHelper(this)

        // Guardar nueva película
        findViewById<Button>(R.id.btnSave).setOnClickListener {
            val title = findViewById<EditText>(R.id.etTitle).text.toString()
            val genre = findViewById<EditText>(R.id.etGenre).text.toString()
            val year = findViewById<EditText>(R.id.etYear).text.toString().toIntOrNull() ?: 0
            val director = findViewById<EditText>(R.id.etDirector).text.toString()
            val duration = findViewById<EditText>(R.id.etDuration).text.toString().toIntOrNull() ?: 0
            val rating = findViewById<EditText>(R.id.etRating).text.toString().toFloatOrNull() ?: 0f
            val description = findViewById<EditText>(R.id.etDescription).text.toString()
            val image = findViewById<EditText>(R.id.etImage).text.toString() // Nueva línea
            if (title.isEmpty() || genre.isEmpty() || year == 0 || director.isEmpty() || duration == 0 || rating == 0f || description.isEmpty() || image.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Crear nueva película
                val newMovie = Movie(0, title, genre, year, director, duration, rating, description, image)
                dbHelper.insertMovie(newMovie)
                Toast.makeText(this, "Movie added", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK) // Devolver resultado
                finish() // Cerrar la actividad
            }
        }
    }
}