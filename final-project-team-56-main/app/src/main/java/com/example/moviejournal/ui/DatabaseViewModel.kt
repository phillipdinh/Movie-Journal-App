package com.example.moviejournal.ui
//
//import android.app.Application
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.viewModelScope
//import com.example.moviejournal.data.AppDatabase
//import com.example.moviejournal.data.ApplicationRepository
//import com.example.moviejournal.data.City
//import kotlinx.coroutines.launch
//
//class DatabaseViewModel(application: Application) : AndroidViewModel(application) {
//    private val repository: ApplicationRepository
//
//    val allJournalEntries: LiveData<List<JournalEntry>>
//
//    init {
//        val journalEntryDao = AppDatabase.getInstance(application).cityDao()
//        repository = ApplicationRepository(cityDao)
//        allCities = repository.allCities
//    }
//
//    fun insert(city: City) {
//        viewModelScope.launch {
//            repository.insert(city)
//        }
//    }
//
//    fun delete(city: City) {
//        viewModelScope.launch {
//            repository.delete(city)
//        }
//    }
//}