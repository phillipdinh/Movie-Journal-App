package com.example.moviejournal.ui

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
//import com.example.moviejournal.R
import com.example.moviejournal.data.JournalEntry
import com.example.moviejournal.viewmodels.JournalViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


class EntryViewFragment : Fragment(com.example.moviejournal.R.layout.fragment_entry_view) {

    private val args by navArgs<EntryEditFragmentArgs>()

    private lateinit var journalViewModel: JournalViewModel

    private lateinit var viewOfLayout: View

    private lateinit var moviePosterIV: ShapeableImageView
    private lateinit var movieTitleTV: MaterialTextView
    private lateinit var userReviewTE: TextInputEditText
    private lateinit var userRatingSlider: Slider
    private lateinit var submitButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewOfLayout = inflater.inflate(com.example.moviejournal.R.layout.fragment_entry_view, container, false)

        //moviePosterIV = viewOfLayout.findViewById(com.example.moviejournal.R.id.iv_movie_poster)

        // for android youtube player
        // youTubePlayerView = viewOfLayout.findViewById(com.example.moviejournal.R.id.youtube_player_view)
        // lifecycle.addObserver(youTubePlayerView)

        movieTitleTV = viewOfLayout.findViewById(com.example.moviejournal.R.id.tv_movie_name)
        userReviewTE = viewOfLayout.findViewById(com.example.moviejournal.R.id.input_user_review)
        userRatingSlider = viewOfLayout.findViewById(com.example.moviejournal.R.id.slider_user_rating)
        submitButton = viewOfLayout.findViewById(com.example.moviejournal.R.id.button_submit)

        return viewOfLayout

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        journalViewModel = ViewModelProvider(requireActivity())[JournalViewModel::class.java]

        movieTitleTV.text = args.journalEntry.movieName
        userReviewTE.setText(args.journalEntry.userReview)
        userRatingSlider.value = args.journalEntry.userRating ?: 5f

        if (args.journalEntry.moviePoster != null) {
            Glide.with(requireContext())
                .load("https://image.tmdb.org/t/p/w200" + args.journalEntry.moviePoster)
                .into(moviePosterIV)
        }

        // need to change to navigate to edit page
        submitButton.setOnClickListener {
            val updatedJournalEntry = JournalEntry(
                args.journalEntry.movieId,
                args.journalEntry.movieName,
                args.journalEntry.moviePoster,
                args.journalEntry.movieRating,
                userReviewTE.text.toString(),
                userRatingSlider.value,
                System.currentTimeMillis()
            )

            journalViewModel.insertJournalEntry(updatedJournalEntry)

            // Navigate back to the beginning page
            findNavController().navigate(com.example.moviejournal.R.id.movie_search)
        }
    }
}