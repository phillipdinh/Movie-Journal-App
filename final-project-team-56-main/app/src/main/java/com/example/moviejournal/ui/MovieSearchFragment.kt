package com.example.moviejournal.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.moviejournal.R
import com.example.moviejournal.viewmodels.MovieSearchViewModel
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText

class MovieSearchFragment : Fragment(R.layout.fragment_movie_search) {

    private lateinit var movieSearchViewModel: MovieSearchViewModel

    // Search related stuff
    private lateinit var submitButton: Button
    private lateinit var searchTextEdit: TextInputEditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieSearchViewModel = ViewModelProvider(requireActivity())[MovieSearchViewModel::class.java]

        // Input stuff
        submitButton = view.findViewById(R.id.button_submit)
        searchTextEdit = view.findViewById(R.id.input_search)

        submitButton.setOnClickListener {
            Log.d(tag, "You want to search for ${searchTextEdit.text}")

            if (searchTextEdit.text.toString().trim() != "") {
                movieSearchViewModel.performMovieSearch(searchTextEdit.text.toString())
                findNavController().navigate(R.id.movie_search_results)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_search, container, false)
    }
}