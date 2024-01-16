package com.adiandrodev.filmadda.data.repositoryimpl.accountstates.datasource

import com.adiandrodev.filmadda.data.model.Country
import com.adiandrodev.filmadda.data.model.MediaList

interface AccountStatesCacheDataSource {

    suspend fun getFavouriteMoviesFromCache(): MediaList?
    suspend fun saveFavouriteMoviesToCache(movies: MediaList?)
    suspend fun getFavouriteTvShowsFromCache(): MediaList?
    suspend fun saveFavouriteTvShowsToCache(tvShows: MediaList?)
    suspend fun getRatedMoviesFromCache(): MediaList?
    suspend fun saveRatedMoviesToCache(movies: MediaList?)
    suspend fun getRatedTvShowsFromCache(): MediaList?
    suspend fun saveRatedTvShowsToCache(tvShows: MediaList?)
    suspend fun getMoviesInWatchlistFromCache(): MediaList?
    suspend fun saveMoviesInWatchlistToCache(movies: MediaList?)
    suspend fun getTvShowsInWatchlistFromCache(): MediaList?
    suspend fun saveTvShowsInWatchlistToCache(tvShows: MediaList?)
    suspend fun getAvailableCountriesFromCache(): ArrayList<Country>?
    suspend fun saveAvailableCountriesToCache(countries: ArrayList<Country>?)
}