package com.example.moviejournal.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviejournal.R
import com.example.moviejournal.data.Movie
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction1

class MovieSearchResultsAdapter(
    private val onNewEntryClick: (Movie) -> Unit,
    private val onAddClick: (Movie) -> Unit,
    private val onRemoveClick: (Movie) -> Unit,
    private val checkWatchlist: KSuspendFunction1<Int, Boolean>
)
    : RecyclerView.Adapter<MovieSearchResultsAdapter.ViewHolder>() {
    var movies: List<Movie> = listOf()

    /**
     * This method is used to update the five-day forecast data stored by this adapter class.
     */

    fun updateMovieList(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }


    override fun getItemCount() = this.movies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_movie_list_item, parent, false)
        return ViewHolder(view, onNewEntryClick, onAddClick, onRemoveClick, checkWatchlist)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(this.movies[position])
    }

    class ViewHolder(
        itemView: View,
        private val onNewEntryClick: (Movie) -> Unit,
        private val onAddClick: (Movie) -> Unit,
        private val onRemoveClick: (Movie) -> Unit,
        private val checkWatchlist: KSuspendFunction1<Int, Boolean>
    )
        : RecyclerView.ViewHolder(itemView) {
        private val movieNameTV: TextView = itemView.findViewById(R.id.tv_movie_name)
        private val releaseDateTV: TextView = itemView.findViewById(R.id.tv_release_date)
        private val ratingTV: TextView = itemView.findViewById(R.id.tv_rating)
        private val posterIV: ImageView = itemView.findViewById(R.id.iv_poster)

        private val buttonNewEntry: MaterialButton = itemView.findViewById(R.id.button_start_entry)
        private val buttonBookmark: MaterialButton = itemView.findViewById(R.id.button_bookmark)

        private lateinit var currentMovie: Movie

        /*
         * Set up a click listener on this individual ViewHolder.  Call the provided onClick
         * function, passing the forecast item represented by this ViewHolder as an argument.
         */

        private var isBookmarked = false
        init {
//            itemView.setOnClickListener {
//                currentMovie.let(onClick)
//            }
            CoroutineScope(Dispatchers.Main).launch {
                Log.d("Adapter", "${currentMovie.id}")
                isBookmarked = checkWatchlist(currentMovie.id)
            }

            buttonBookmark.setOnClickListener {
                if (isBookmarked) {
                    Toast.makeText(itemView.context, "Added to watchlist", Toast.LENGTH_SHORT).show()
                    onAddClick(currentMovie)
                    buttonBookmark.setIconResource(R.drawable.ic_action_remove)
                } else {
                    Toast.makeText(itemView.context, "Removed from watchlist", Toast.LENGTH_SHORT).show()
                    onRemoveClick(currentMovie)
                    buttonBookmark.setIconResource(R.drawable.ic_action_add)
                }
                isBookmarked = !isBookmarked // toggle isBookmarked when clicked

            }
            buttonNewEntry.setOnClickListener {
                currentMovie.let(onNewEntryClick)
            }
        }

        fun bind(movie: Movie) {

            currentMovie = movie

            val ctx = itemView.context
            movieNameTV.text = currentMovie.title
            releaseDateTV.text = "Released ${currentMovie.release_date}"
            ratingTV.text = "Rating: ${(currentMovie.vote_average * 10).toInt().toFloat() / 10} / 10"

            CoroutineScope(Dispatchers.Main).launch {
                isBookmarked = checkWatchlist(movie.id)
                buttonBookmark.setIconResource(if (isBookmarked) R.drawable.ic_action_add else R.drawable.ic_action_remove) // toggle the bookmark button icon based on isBookmarked
            }

            /*
             * Load forecast icon into ImageView using Glide: https://bumptech.github.io/glide/
             */
            if (currentMovie.poster_path != null) {
                Glide.with(ctx)
                    .load("https://image.tmdb.org/t/p/w200" + currentMovie.poster_path)
                    .into(posterIV)
            }

        }
    }
}