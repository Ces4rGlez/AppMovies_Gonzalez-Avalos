package mx.edu.utng.sitiomovies
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "MoviesDB"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "movies"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_GENRE = "genre"
        private const val COLUMN_YEAR = "year"
        private const val COLUMN_DIRECTOR = "director"
        private const val COLUMN_DURATION = "duration"
        private const val COLUMN_RATING = "rating"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_IMAGE = "image"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_TITLE TEXT,"
                + "$COLUMN_GENRE TEXT,"
                + "$COLUMN_YEAR INTEGER,"
                + "$COLUMN_DIRECTOR TEXT,"
                + "$COLUMN_DURATION INTEGER,"
                + "$COLUMN_RATING REAL,"
                + "$COLUMN_DESCRIPTION TEXT,"  // Aquí debe ir una coma, no un cierre de paréntesis
                + "$COLUMN_IMAGE TEXT"  // Ahora la columna image está correctamente declarada
                + ")")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Insertar una película
    fun insertMovie(movie: Movie): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, movie.title)
            put(COLUMN_GENRE, movie.genre)
            put(COLUMN_YEAR, movie.year)
            put(COLUMN_DIRECTOR, movie.director)
            put(COLUMN_DURATION, movie.duration)
            put(COLUMN_RATING, movie.rating)
            put(COLUMN_DESCRIPTION, movie.description)
            put(COLUMN_IMAGE, movie.image)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    // Obtener todas las películas
    fun getAllMovies(): List<Movie> {
        val movieList = mutableListOf<Movie>()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                val movie = Movie(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENRE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_YEAR)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIRECTOR)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DURATION)),
                    cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_RATING)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE))

                )
                movieList.add(movie)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return movieList
    }
    fun updateMovieRating(movieId: Int, rating: Float): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_RATING, rating)
        }
        return db.update(TABLE_NAME, values, "$COLUMN_ID=?", arrayOf(movieId.toString()))
    }

    fun getRecommendedMovies(genre: String, minRating: Float): List<Movie> {
        val movieList = mutableListOf<Movie>()
        val db = this.readableDatabase

        val query = if (genre.isEmpty()) {
            // Si no se especifica género, solo filtrar por rating
            "SELECT * FROM $TABLE_NAME WHERE $COLUMN_RATING >= ?"
        } else {
            // Si se especifica género, filtrar por género y rating
            "SELECT * FROM $TABLE_NAME WHERE $COLUMN_GENRE = ? AND $COLUMN_RATING >= ?"
        }

        val args = if (genre.isEmpty()) {
            arrayOf(minRating.toString())
        } else {
            arrayOf(genre, minRating.toString())
        }

        val cursor: Cursor = db.rawQuery(query, args)

        if (cursor.moveToFirst()) {
            do {
                val movie = Movie(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENRE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_YEAR)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIRECTOR)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DURATION)),
                    cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_RATING)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE))
                )
                movieList.add(movie)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return movieList
    }


    // Actualizar una película
    fun updateMovie(movie: Movie): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, movie.title)
            put(COLUMN_GENRE, movie.genre)
            put(COLUMN_YEAR, movie.year)
            put(COLUMN_DIRECTOR, movie.director)
            put(COLUMN_DURATION, movie.duration)
            put(COLUMN_RATING, movie.rating)
            put(COLUMN_DESCRIPTION, movie.description)
            put(COLUMN_IMAGE, movie.image)
        }
        return db.update(TABLE_NAME, values, "$COLUMN_ID=?", arrayOf(movie.id.toString()))
    }

    // Eliminar una película
    fun deleteMovie(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(id.toString()))
    }
    fun getMovieById(id: Int): Movie? {
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = ?", arrayOf(id.toString()))

        return if (cursor.moveToFirst()) {
            val movie = Movie(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENRE)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_YEAR)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIRECTOR)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DURATION)),
                cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_RATING)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE))


            )
            cursor.close()
            movie
        } else {
            cursor.close()
            null // Devolver null si no se encuentra la película
        }
    }
}