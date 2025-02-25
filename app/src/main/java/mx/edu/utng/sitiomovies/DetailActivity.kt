package mx.edu.utng.sitiomovies

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var movie: Movie

    // ActivityResultLauncher para EditActivity
    private val editMovieLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            // Recargar los detalles de la película
            movie = dbHelper.getMovieById(movie.id)!!
            updateUI()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        dbHelper = DatabaseHelper(this)
        movie = intent.getParcelableExtra<Movie>("MOVIE")!!

        // Mostrar detalles de la película
        updateUI()

        // Botón para editar
        findViewById<Button>(R.id.btnEdit).setOnClickListener {
            val intent = Intent(this, EditActivity::class.java).apply {
                putExtra("MOVIE", movie)
            }
            editMovieLauncher.launch(intent)
        }

        // Botón para eliminar
        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            dbHelper.deleteMovie(movie.id)
            Toast.makeText(this, "Movie deleted", Toast.LENGTH_SHORT).show()
            finish()
        }
        val movieFromDb = dbHelper.getMovieById(movie.id)
        if (movieFromDb != null) {
            movie = movieFromDb // Actualizar el objeto movie con los datos de la base de datos
        }

        // Mostrar detalles de la película
        updateUI()


}


    fun rateMovie(view: View) {
        val rating = view.tag.toString().toInt() // Obtener la calificación del tag del ImageView
        val movieId = movie.id // Obtener el ID de la película actual

        // Actualizar la calificación en la base de datos
        val rowsUpdated = dbHelper.updateMovieRating(movieId, rating.toFloat())

        if (rowsUpdated > 0) {
            // Actualizar las estrellas en la interfaz
            updateStars(rating)
            Toast.makeText(this, "Rating saved: $rating stars", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Failed to save rating", Toast.LENGTH_SHORT).show()
        }
    }




    private fun updateStars(rating: Int) {
        val stars = listOf(
            findViewById<ImageView>(R.id.ivStar1),
            findViewById<ImageView>(R.id.ivStar2),
            findViewById<ImageView>(R.id.ivStar3),
            findViewById<ImageView>(R.id.ivStar4),
            findViewById<ImageView>(R.id.ivStar5)
        )

        for (i in 0 until 5) {
            if (i < rating) {
                stars[i].setImageResource(R.drawable.ic_star_filled)
            } else {
                stars[i].setImageResource(R.drawable.ic_star_outline)
            }
        }
    }

    private fun updateUI() {
        Glide.with(this)
            .load(movie.image)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.error)
            .into(findViewById(R.id.ivDetailImage))

        findViewById<TextView>(R.id.tvDetailTitle).text = movie.title
        findViewById<TextView>(R.id.tvDetailGenre).text = "${movie.genre}"
        findViewById<TextView>(R.id.tvDetailYear).text = "${movie.year}"
        findViewById<TextView>(R.id.tvDetailDirector).text = "${movie.director}"
        findViewById<TextView>(R.id.tvDetailDuration).text = "${movie.duration} mins"
        findViewById<TextView>(R.id.tvDetailRating).text = "${movie.rating}"
        findViewById<TextView>(R.id.tvDetailDescription).text = "${movie.description}"

        updateStars(movie.rating.toInt())
    }
}