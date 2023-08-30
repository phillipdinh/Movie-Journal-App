package com.example.moviejournal.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviejournal.R
import com.example.moviejournal.data.Movie
import com.example.moviejournal.data.SavedMovie
import com.google.android.material.button.MaterialButton

class WatchlistAdapter(
    private val onNewEntryClick: (SavedMovie) -> Unit,
    private val onBookmarkClick: (SavedMovie) -> Unit)
    : RecyclerView.Adapter<WatchlistAdapter.ViewHolder>() {
    var movies: List<SavedMovie> = listOf()

    /**
     * This method is used to update the five-day forecast data stored by this adapter class.
     */
    fun updateWatchlist(newMovies: List<SavedMovie>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    override fun getItemCount() = if (this.movies.size % 2 == 0) {
        this.movies.size / 2
    } else {
        (this.movies.size + 1) / 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_watchlist_item, parent, false)
        return ViewHolder(view, onNewEntryClick, onBookmarkClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val leftMovieIndex = position * 2
        val rightMovieIndex = leftMovieIndex + 1

        if (rightMovieIndex < this.movies.size) {
            holder.bindLeft(this.movies[leftMovieIndex])
            holder.bindRight(this.movies[rightMovieIndex])
        } else {
            holder.bindLeft(this.movies[leftMovieIndex])
            holder.hideRightButton ()
        }
    }
    class ViewHolder(itemView: View,
                     private val onNewEntryClick: (SavedMovie) -> Unit,
                     private val onBookmarkClick: (SavedMovie) -> Unit)
        : RecyclerView.ViewHolder(itemView) {

        private val posterIVLeft: ImageView = itemView.findViewById(R.id.iv_left_poster)
        private val posterIVRight: ImageView = itemView.findViewById(R.id.iv_right_poster)
        private val buttonLeft: MaterialButton = itemView.findViewById(R.id.button_left)
        private val buttonRight: MaterialButton = itemView.findViewById(R.id.button_right)


        fun bindLeft(movie: SavedMovie) {

            posterIVLeft.setOnClickListener {
                movie.let(onNewEntryClick)
            }
            buttonLeft.setOnClickListener {
                movie.let(onBookmarkClick)
            }
            val ctx = itemView.context

            if (movie.moviePoster != null) {
                Glide.with(ctx)
                    .load("https://image.tmdb.org/t/p/w200" + movie.moviePoster)
                    .into(posterIVLeft)
            }
        }

        fun bindRight(movie: SavedMovie) {

            posterIVRight.setOnClickListener {
                movie.let(onNewEntryClick)
            }
            buttonRight.setOnClickListener {
                movie.let(onBookmarkClick)
            }
            val ctx = itemView.context

            if (movie.moviePoster != null) {
                Glide.with(ctx)
                    .load("https://image.tmdb.org/t/p/w200" + movie.moviePoster)
                    .into(posterIVRight)
            }


        }

        fun hideRightButton (){
            posterIVRight.visibility = View.INVISIBLE
            buttonRight.visibility = View.INVISIBLE // hide the right button
        }
        /*private val movieNameTV: TextView = itemView.findViewById(R.id.tv_movie_name)


        private val buttonNewEntry: MaterialButton = itemView.findViewById(R.id.button_start_entry)
        private val buttonBookmark: MaterialButton = itemView.findViewById(R.id.button_bookmark)
        */

        /*
         * Set up a click listener on this individual ViewHolder.  Call the provided onClick
         * function, passing the forecast item represented by this ViewHolder as an argument.
         */
        /*init {
//            itemView.setOnClickListener {
//                currentMovie.let(onClick)
//            }
            buttonBookmark.setOnClickListener {
                currentMovie.let(onBookmarkClick)
            }

            buttonNewEntry.setOnClickListener {
                currentMovie.let(onNewEntryClick)
            }
        }*/

        /*
        fun bind(movie: SavedMovie) {
            currentMovie = movie

            val ctx = itemView.context
            //movieNameTV.text = currentMovie.movieName
            /*
             * Load forecast icon into ImageView using Glide: https://bumptech.github.io/glide/
             */
            if (currentMovie.moviePoster != null) {
                Glide.with(ctx)
                    .load("https://image.tmdb.org/t/p/w200" + currentMovie.moviePoster)
                    .into(posterIV)
            }
        }

         */
    }
}
