package com.example.moviejournal.ui

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.moviejournal.R
import com.example.moviejournal.data.JournalEntry
import com.example.moviejournal.data.Movie
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout.Tab

class JournalAdapter(
    private val onClick: (JournalEntry) -> Unit
) : RecyclerView.Adapter<JournalAdapter.ViewHolder>() {
    var journalEntries: List<JournalEntry> = listOf()
    var sortModeTab: Tab? = null

    fun updateJournalEntryList(newJournalEntries: List<JournalEntry>) {
        journalEntries = newJournalEntries
        applySortMode()
        notifyDataSetChanged()
    }

    fun updateSortModeTab(newTab: Tab) {
        sortModeTab = newTab
        applySortMode()
        notifyDataSetChanged()
    }

    private fun applySortMode() {
        when(sortModeTab?.text) {
            "Alphabetical" -> {
                journalEntries = journalEntries.sortedBy { it.movieName }
            }
            "Highest Rated" -> {
                journalEntries = journalEntries.sortedBy { it.userRating }.reversed()
            }
            "Lowest Rated" -> {
                journalEntries = journalEntries.sortedBy { it.userRating }
            }
            "Recent" -> {
                journalEntries = journalEntries.sortedBy { it.timestamp }.reversed()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_journal_list_item, parent, false)
        return JournalAdapter.ViewHolder(view, onClick)
    }

    override fun getItemCount(): Int = journalEntries.size

    override fun onBindViewHolder(holder: JournalAdapter.ViewHolder, position: Int) {
        holder.bind(this.journalEntries[position])
    }

    class ViewHolder(itemView: View, private val onClick: (JournalEntry) -> Unit)
        : RecyclerView.ViewHolder(itemView) {
        private val movieNameTV: TextView = itemView.findViewById(R.id.tv_movie_name)
        private val userRatingTV: TextView = itemView.findViewById(R.id.tv_user_rating)
        private val crticRatingTV: TextView = itemView.findViewById(R.id.tv_critic_rating)
        private val posterIV: ImageView = itemView.findViewById(R.id.iv_poster)

        private val viewEntryButton: MaterialButton = itemView.findViewById(R.id.button_open_entry)

        private lateinit var currentJournalEntry: JournalEntry

        /*
         * Set up a click listener on this individual ViewHolder.  Call the provided onClick
         * function, passing the forecast item represented by this ViewHolder as an argument.
         */
        init {
//            itemView.setOnClickListener {
//                currentJournalEntry.let(onClick)
//            }

            viewEntryButton.setOnClickListener {
                currentJournalEntry.let(onClick)
            }
        }

        fun bind(journalEntry: JournalEntry) {
            currentJournalEntry = journalEntry

            val criticRating = formatRating(journalEntry.movieRating)
            val ctx = itemView.context
            movieNameTV.text = currentJournalEntry.movieName
            crticRatingTV.text = criticRating
            userRatingTV.text = "${currentJournalEntry.userRating} / 10"


            if (currentJournalEntry.moviePoster != null) {
                Glide.with(ctx)
                    .load("https://image.tmdb.org/t/p/w200" + currentJournalEntry.moviePoster)
                    .into(posterIV)
            }
        }
        fun formatRating(rating: Float): String {
            val roundedRating = String.format("%.1f", rating)
            return "$roundedRating/10"
        }
    }
}