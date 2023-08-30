package com.example.moviejournal.data

import com.example.moviejournal.api.MovieDatabaseService
import com.example.moviejournal.ui.TMDB_API_KEY
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository (
    private val service: MovieDatabaseService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private var cachedMovieQuery: MovieSearchResults? = null
    private var cachedQuery: String? = null

    suspend fun searchMovies (
        query: String?,
    ) : Result<MovieSearchResults> {
        return if (query == cachedQuery && cachedMovieQuery != null) {
            Result.success(cachedMovieQuery!!)
        } else {
            cachedQuery = query
            withContext(ioDispatcher) {
                try {
                    val response = service.queryMoviesByName(TMDB_API_KEY, query)
                    if (response.isSuccessful) {
                        cachedMovieQuery = response.body()
                        Result.success(cachedMovieQuery!!)
                    } else {
                        Result.failure(Exception(response.errorBody()?.string()))
                    }
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }
        }
    }
}