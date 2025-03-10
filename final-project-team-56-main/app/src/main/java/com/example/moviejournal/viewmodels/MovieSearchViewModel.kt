package com.example.moviejournal.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviejournal.api.MovieDatabaseService
import com.example.moviejournal.data.Movie
import com.example.moviejournal.data.MovieRepository
import kotlinx.coroutines.launch

/**
 * This is a ViewModel class that holds 5-day/3-hour forecast data for the main activity.
 */
class MovieSearchViewModel: ViewModel() {
    private val repository = MovieRepository(MovieDatabaseService.create())

    /*
     * The most recent response from the OpenWeather 5-day/3-hour forecast API are stored in this
     * private property.  These results are exposed to the outside world in immutable form via the
     * public `forecast` property below.
     */
    private val _movies = MutableLiveData<List<Movie>?>(null)

    /**
     * This value provides the most recent response from the OpenWeather 5-day/3-hour forecast API.
     * It is null if there are no current results (e.g. in the case of an error).
     */
    val movies: LiveData<List<Movie>?> = _movies

    /*
     * The current error for the most recent API query is stored in this private property.  This
     * error is exposed to the outside world in immutable form via the public `error` property
     * below.
     */
    private val _error = MutableLiveData<Throwable?>(null)

    /**
     * This property provides the error associated with the most recent API query, if there is
     * one.  If there was no error associated with the most recent API query, it will be null.
     */
    val error: LiveData<Throwable?> = _error

    /*
     * The current loading state is stored in this private property.  This loading state is exposed
     * to the outside world in immutable form via the public `loading` property below.
     */
    private val _loading = MutableLiveData<Boolean>(false)

    /**
     * This property indicates the current loading state of an API query.  It is `true` if an
     * API query is currently being executed or `false` otherwise.
     */
    val loading: LiveData<Boolean> = _loading

    /**
     * This method triggers a new call to the OpenWeather API's 5-day/3-hour forecast method.
     * It updates the public properties of this ViewModel class to reflect the current status
     * of the API query.
     *
     * @param location Specifies the location for which to fetch forecast data.  For US cities,
     *   this should be specified as "<city>,<state>,<country>" (e.g. "Corvallis,OR,US"), while
     *   for international cities, it should be specified as "<city>,<country>" (e.g. "London,GB").
     * @param units Specifies the type of units that should be returned by the OpenWeather API.
     *   Can be one of: "standard", "metric", and "imperial".
     * @param apiKey Should be a valid OpenWeather API key.
     */

    fun performMovieSearch(query: String) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.searchMovies(query)
            _loading.value = false
            _error.value = result.exceptionOrNull()
            _movies.value = result.getOrNull()?.results?.sortedByDescending { it.popularity }
        }
    }

//    fun loadFiveDayForecast(location: String?, units: String?, apiKey: String) {
//        /*
//         * Launch a new coroutine in which to execute the API call.  The coroutine is tied to the
//         * lifecycle of this ViewModel by using `viewModelScope`.
//         */
//
//        Log.d("MainActivity", "Loading forecast for $location!")
//        viewModelScope.launch {
//            _loading.value = true
//            val result = repository.loadFiveDayForecast(location, units, apiKey)
//            _loading.value = false
//            _error.value = result.exceptionOrNull()
//            _movies.value = result.getOrNull()
//        }
//    }
}