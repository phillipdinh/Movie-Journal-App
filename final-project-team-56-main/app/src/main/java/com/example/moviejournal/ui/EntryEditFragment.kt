package com.example.moviejournal.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.moviejournal.R
import com.example.moviejournal.data.JournalEntry
import com.example.moviejournal.viewmodels.JournalViewModel
import com.example.moviejournal.viewmodels.MovieSearchViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView


class EntryEditFragment : Fragment(R.layout.fragment_entry_edit) {

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
        viewOfLayout = inflater.inflate(R.layout.fragment_entry_edit, container, false)

        moviePosterIV = viewOfLayout.findViewById(R.id.iv_movie_poster)
        movieTitleTV = viewOfLayout.findViewById(R.id.tv_movie_name)
        userReviewTE = viewOfLayout.findViewById(R.id.input_user_review)
        userRatingSlider = viewOfLayout.findViewById(R.id.slider_user_rating)
        submitButton = viewOfLayout.findViewById(R.id.button_submit)

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
            findNavController().navigate(R.id.movie_search)
        }
    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment EntryEditFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
////            EntryEditFragment().apply {
////                arguments = Bundle().apply {
////                    putString(ARG_PARAM1, param1)
////                    putString(ARG_PARAM2, param2)
////                }
////            }
//    }
}