package com.example.moviejournal.data


import com.squareup.moshi.JsonClass

// GET
// /search/movie
// https://developers.themoviedb.org/3/search/search-movies
@JsonClass(generateAdapter = true)
data class Movie(
    val poster_path: String?,
    val adult: Boolean,
    val overview: String,
    val release_date: String,
    val genre_ids: List<Int>,
    val id: Int,
    val original_title: String,
    val original_language: String,
    val title: String,
    val backdrop_path: String?,
    val popularity: Float,
    val vote_count: Int,
    val video: Boolean,
    val vote_average: Float
) : java.io.Serializable

@JsonClass(generateAdapter = true)
data class MovieSearchResults(
    val page: Int,
    val results: List<Movie>,
    val total_results: Int,
    val total_pages: Int
)