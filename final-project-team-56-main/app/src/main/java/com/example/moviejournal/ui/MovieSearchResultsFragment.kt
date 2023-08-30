package com.example.moviejournal.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviejournal.R
import com.example.moviejournal.data.JournalEntry
import com.example.moviejournal.data.Movie
import com.example.moviejournal.data.SavedMovie
import com.example.moviejournal.viewmodels.MovieSearchViewModel
import com.example.moviejournal.viewmodels.WatchlistViewModel
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.coroutines.*

class MovieSearchResultsFragment : Fragment(R.layout.fragment_movie_list) {
//    private val tag = "MainActivity"


    private val movieSearchResultsAdapter = MovieSearchResultsAdapter(
        ::onNewEntryClick,
        ::onAddClick,
        ::onRemoveClick,
        ::checkWatchlist)

    private lateinit var movieListRV: RecyclerView
    private lateinit var loadingErrorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator
    private lateinit var emptyTV: TextView

    private lateinit var sharedViewModel: MovieSearchViewModel
    private lateinit var watchlistViewModel: WatchlistViewModel

//    private val args by navArgs<MovieSearchFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        watchlistViewModel = ViewModelProvider(requireActivity())[WatchlistViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity())[MovieSearchViewModel::class.java]

        loadingErrorTV = view.findViewById(R.id.tv_loading_error)
        loadingIndicator = view.findViewById(R.id.loading_indicator)
        emptyTV = view.findViewById(R.id.tv_empty)

        /*
         * Set up RecyclerView.
         */
        movieListRV = view.findViewById(R.id.rv_movie_list)
        movieListRV.layoutManager = LinearLayoutManager(requireContext())
        movieListRV.setHasFixedSize(true)
        movieListRV.adapter = movieSearchResultsAdapter

        /*
         * Set up an observer on the current forecast data.  If the forecast is not null, display
         * it in the UI.
         */
        sharedViewModel.movies.observe(viewLifecycleOwner) { movies ->
            if (movies != null) {
                movieSearchResultsAdapter.updateMovieList(movies)
                movieListRV.visibility = View.VISIBLE
                movieListRV.scrollToPosition(0)
//                Log.d("MainActivity", "We observed a change for ${forecast.city.name}")
//                setNewTitle(forecast.city.name)
            }
        }

        /*
         * Set up an observer on the error associated with the current API call.  If the error is
         * not null, display the error that occurred in the UI.
         */
        sharedViewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                loadingErrorTV.text = getString(R.string.loading_error, error.message)
                loadingErrorTV.visibility = View.VISIBLE
                Log.e(tag, "Error fetching forecast: ${error.message}")
            }
        }

        /*
         * Set up an observer on the loading status of the API query.  Display the correct UI
         * elements based on the current loading status.
         */
        sharedViewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                loadingIndicator.visibility = View.VISIBLE
                loadingErrorTV.visibility = View.INVISIBLE
                movieListRV.visibility = View.INVISIBLE
            } else {
                loadingIndicator.visibility = View.INVISIBLE
            }
        }

        // Add a listener to the adapter's data set to show/hide the empty view
        movieSearchResultsAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                if (movieSearchResultsAdapter.itemCount == 0) {
                    emptyTV.visibility = View.VISIBLE
                    movieListRV.visibility = View.GONE
                } else {
                    emptyTV.visibility = View.GONE
                    movieListRV.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun onAddClick(movie: Movie){
        val savedMovie = SavedMovie(
            movie.id,
            movie.title,
            movie.poster_path,
            movie.vote_average,
            System.currentTimeMillis()
        )
        watchlistViewModel.insertSavedMovie(savedMovie)
        //return watchlistViewModel.getBy
    }
    private fun onRemoveClick(movie: Movie){
        val savedMovie = SavedMovie(
            movie.id,
            movie.title,
            movie.poster_path,
            movie.vote_average,
            System.currentTimeMillis()
        )
        watchlistViewModel.deleteSavedMovie(savedMovie)
    }

    suspend fun checkWatchlist(movieId: Int): Boolean = coroutineScope {
        async {

            val savedMovie = watchlistViewModel.getSavedMovieById(movieId)
            savedMovie != null
        }.await()
    }

    private fun onNewEntryClick(movie: Movie) {
        var journalEntry = JournalEntry(
            movie.id,
            movie.title,
            movie.poster_path,
            movie.vote_average,
            null,
            null,
            System.currentTimeMillis()
        )

        val directions = MovieSearchResultsFragmentDirections.navigateToEntryEdit(journalEntry)
        findNavController().navigate(directions)
    }
}