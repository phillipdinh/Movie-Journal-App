package com.example.moviejournal.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.moviejournal.data.AppDatabase
import com.example.moviejournal.data.ApplicationRepository
import com.example.moviejournal.data.JournalEntry
import kotlinx.coroutines.launch

class JournalViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ApplicationRepository<JournalEntry>

    val allJournalEntries: LiveData<List<JournalEntry>>

    //TODO: Add SavedMovie
    init {
        val journalEntryDao = AppDatabase.getInstance(application).journalEntryDao()
        repository = ApplicationRepository(journalEntryDao)
        allJournalEntries = repository.allItems
    }

    fun insertJournalEntry(journalEntry: JournalEntry) {
        viewModelScope.launch {
            repository.insert(journalEntry)
        }
    }

    fun deleteJournalEntry(journalEntry: JournalEntry) {
        viewModelScope.launch {
            repository.delete(journalEntry)
        }
    }
}