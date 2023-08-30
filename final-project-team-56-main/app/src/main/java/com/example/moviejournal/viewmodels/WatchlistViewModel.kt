package com.example.moviejournal.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.moviejournal.data.AppDatabase
import com.example.moviejournal.data.ApplicationRepository
import com.example.moviejournal.data.SavedMovie
import kotlinx.coroutines.launch

class WatchlistViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ApplicationRepository<SavedMovie>

    val allSavedMovies: LiveData<List<SavedMovie>>

    //TODO: Add SavedMovie
    init {
        val savedMovieDao = AppDatabase.getInstance(application).savedMovieDao()
        repository = ApplicationRepository(savedMovieDao)
        allSavedMovies = repository.allItems
    }

    fun insertSavedMovie(savedMovie: SavedMovie) {
        viewModelScope.launch {
            repository.insert(savedMovie)
        }
    }

    fun deleteSavedMovie(savedMovie: SavedMovie) {
        viewModelScope.launch {
            repository.delete(savedMovie)
        }
    }

    suspend fun getSavedMovieById(id: Int): LiveData<SavedMovie> {
        return repository.getById(id)
    }
}