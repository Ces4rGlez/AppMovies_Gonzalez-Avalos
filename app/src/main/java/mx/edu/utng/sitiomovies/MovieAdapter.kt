package mx.edu.utng.sitiomovies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
class MovieAdapter(private val movieList: List<Movie>, private val onItemClick: (Movie) -> Unit) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieList[position]
        holder.bind(movie)
        holder.itemView.setOnClickListener { onItemClick(movie) }
    }

    override fun getItemCount(): Int = movieList.size

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.ivMovieImage)
        private val title: TextView = itemView.findViewById(R.id.tvTitle)
        private val genre: TextView = itemView.findViewById(R.id.tvGenre)
        private val year: TextView = itemView.findViewById(R.id.tvYear)

        fun bind(movie: Movie) {

            Glide.with(itemView.context)
                .load(movie.image) // URL o recurso local
                .placeholder(R.drawable.placeholder) // Imagen de placeholder
                .error(R.drawable.error) // Imagen de error
                .into(image)

            title.text = movie.title
            genre.text = movie.genre
            year.text = movie.year.toString()
        }
    }
}