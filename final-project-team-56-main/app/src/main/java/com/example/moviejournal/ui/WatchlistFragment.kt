package com.example.moviejournal.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviejournal.R
import com.example.moviejournal.data.JournalEntry
import com.example.moviejournal.data.SavedMovie
import com.example.moviejournal.viewmodels.WatchlistViewModel
import com.google.android.material.progressindicator.CircularProgressIndicator

class WatchlistFragment : Fragment(R.layout.fragment_watchlist) {
    private lateinit var watchlistViewModel: WatchlistViewModel

    private val watchlistAdapter = WatchlistAdapter(::onNewEntryClick, ::onBookmarkClick)


    private lateinit var watchlistRV: RecyclerView
    private lateinit var loadingErrorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator
    private lateinit var emptyTV: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        watchlistViewModel = ViewModelProvider(requireActivity())[WatchlistViewModel::class.java]

        loadingErrorTV = view.findViewById(R.id.tv_loading_error)
        loadingIndicator = view.findViewById(R.id.loading_indicator)
        emptyTV = view.findViewById(R.id.tv_empty)

        /*
     * Set up RecyclerView.
     */
        watchlistRV = view.findViewById(R.id.rv_watchlist)
        watchlistRV.layoutManager = LinearLayoutManager(requireContext())
        watchlistRV.setHasFixedSize(true)

        watchlistRV.adapter = watchlistAdapter

        watchlistViewModel.allSavedMovies.observe(viewLifecycleOwner) { allSavedMovies ->
            if (allSavedMovies != null) {
                //TODO add function to adapter
                watchlistAdapter.updateWatchlist(allSavedMovies.sortedByDescending { it.timestamp })
                watchlistRV.visibility = View.VISIBLE
                watchlistRV.scrollToPosition(0)
            }
        }


        // Add a listener to the adapter's data set to show/hide the empty view
        watchlistAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                if (watchlistAdapter.itemCount == 0) {
                    emptyTV.visibility = View.VISIBLE
                    watchlistRV.visibility = View.GONE
                } else {
                    emptyTV.visibility = View.GONE
                    watchlistRV.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun onBookmarkClick(movie: SavedMovie){
        val savedMovie = SavedMovie(
            movie.movieId,
            movie.movieName,
            movie.moviePoster,
            movie.movieRating,
            System.currentTimeMillis()
        )
        watchlistViewModel.deleteSavedMovie(savedMovie)
    }
    private fun onNewEntryClick(movie: SavedMovie) {
        var journalEntry = JournalEntry(
            movie.movieId,
            movie.movieName,
            movie.moviePoster,
            movie.movieRating,
            null,
            null,
            System.currentTimeMillis()
        )

        val directions = MovieSearchResultsFragmentDirections.navigateToEntryEdit(journalEntry)
        findNavController().navigate(directions)
    }



}