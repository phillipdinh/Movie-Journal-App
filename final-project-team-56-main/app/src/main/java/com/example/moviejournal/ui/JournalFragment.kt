package com.example.moviejournal.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.example.moviejournal.R
import com.example.moviejournal.data.JournalEntry
import com.example.moviejournal.data.Movie
import com.example.moviejournal.viewmodels.JournalViewModel
import com.example.moviejournal.viewmodels.MovieSearchViewModel
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
import com.google.android.material.textfield.TextInputEditText

/**
 * A fragment representing a list of Items.
 */
class JournalFragment : Fragment(R.layout.fragment_journal_list) {

    private lateinit var journalViewModel: JournalViewModel

    private val journalAdapter = JournalAdapter(::onJournalItemClick)

    private lateinit var journalEntryListRV: RecyclerView
    private lateinit var loadingErrorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator
    private lateinit var sortModeTab: TabLayout

    private lateinit var entriesButton: Button
    private lateinit var entriesTextEdit: TextInputEditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        journalViewModel = ViewModelProvider(requireActivity())[JournalViewModel::class.java]

        loadingErrorTV = view.findViewById(R.id.tv_loading_error)
        loadingIndicator = view.findViewById(R.id.loading_indicator)
        sortModeTab = view.findViewById(R.id.tab_sort_mode)

        entriesButton = view.findViewById(R.id.filter_button_submit)
        entriesTextEdit = view.findViewById(R.id.filter_search)

        sortModeTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: Tab?) {
                Log.d(tag, "onTabSelected called for ${tab?.text}")
                if (tab != null) {
                    journalAdapter.updateSortModeTab(tab)
                }
            }

            override fun onTabUnselected(tab: Tab?) {
            }

            override fun onTabReselected(tab: Tab?) {
            }
        })

        entriesButton.setOnClickListener {
            Log.d(tag, "you want to look for the entry ${entriesTextEdit.text.toString()}")

            //if ()
        }

        val preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val defaultSortMethod = preferences.getString("pref_default_sort_method", null)

        if (defaultSortMethod != null) {
            when(defaultSortMethod) {
                "Alphabetical" -> sortModeTab.selectTab(sortModeTab.getTabAt(0))
                "Recent" -> sortModeTab.selectTab(sortModeTab.getTabAt(1))
                "High" -> sortModeTab.selectTab(sortModeTab.getTabAt(2))
                "Low" -> sortModeTab.selectTab(sortModeTab.getTabAt(3))
            }
        }

        if (journalAdapter.sortModeTab != null) {
            sortModeTab.selectTab(journalAdapter.sortModeTab)
        }

        /*
         * Set up RecyclerView.
         */
        journalEntryListRV = view.findViewById(R.id.rv_journal_list)
        journalEntryListRV.layoutManager = LinearLayoutManager(requireContext())
        journalEntryListRV.setHasFixedSize(true)
        journalEntryListRV.adapter = journalAdapter

        /*
         * Set up an observer on the current forecast data.  If the forecast is not null, display
         * it in the UI.
         */
        journalViewModel.allJournalEntries.observe(viewLifecycleOwner) { allJournalEntries ->
            if (allJournalEntries != null) {
                journalAdapter.updateJournalEntryList(allJournalEntries.sortedBy { it.movieName })
                journalEntryListRV.visibility = View.VISIBLE
                journalEntryListRV.scrollToPosition(0)
            }
        }
    }

    private fun onJournalItemClick(journalEntry: JournalEntry) {
        val directions = MovieSearchResultsFragmentDirections.navigateToEntryEdit(journalEntry)
        findNavController().navigate(directions)
    }
}