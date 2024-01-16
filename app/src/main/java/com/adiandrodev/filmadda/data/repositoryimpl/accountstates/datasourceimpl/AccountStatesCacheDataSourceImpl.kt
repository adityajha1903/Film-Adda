package com.adiandrodev.filmadda.data.repositoryimpl.accountstates.datasourceimpl

import com.adiandrodev.filmadda.data.model.Country
import com.adiandrodev.filmadda.data.model.MediaList
import com.adiandrodev.filmadda.data.repositoryimpl.accountstates.datasource.AccountStatesCacheDataSource

class AccountStatesCacheDataSourceImpl: AccountStatesCacheDataSource {

    private var favouriteMovies: MediaList? = null
    override suspend fun getFavouriteMoviesFromCache(): MediaList? {
        return favouriteMovies
    }
    override suspend fun saveFavouriteMoviesToCache(movies: MediaList?) {
        favouriteMovies = movies
    }

    private var favouriteTvShows: MediaList? = null
    override suspend fun getFavouriteTvShowsFromCache(): MediaList? {
        return favouriteTvShows
    }
    override suspend fun saveFavouriteTvShowsToCache(tvShows: MediaList?) {
        favouriteTvShows = tvShows
    }

    private var ratedMovies: MediaList? = null
    override suspend fun getRatedMoviesFromCache(): MediaList? {
        return ratedMovies
    }
    override suspend fun saveRatedMoviesToCache(movies: MediaList?) {
        ratedMovies = movies
    }

    private var ratedTvShows: MediaList? = null
    override suspend fun getRatedTvShowsFromCache(): MediaList? {
        return ratedTvShows
    }
    override suspend fun saveRatedTvShowsToCache(tvShows: MediaList?) {
        ratedTvShows = tvShows
    }

    private var watchlistMovies: MediaList? = null
    override suspend fun getMoviesInWatchlistFromCache(): MediaList? {
        return watchlistMovies
    }
    override suspend fun saveMoviesInWatchlistToCache(movies: MediaList?) {
        watchlistMovies = movies
    }

    private var watchlistTvShows: MediaList? = null
    override suspend fun getTvShowsInWatchlistFromCache(): MediaList? {
        return watchlistTvShows
    }
    override suspend fun saveTvShowsInWatchlistToCache(tvShows: MediaList?) {
        watchlistTvShows = tvShows
    }

    private var countries: ArrayList<Country>? = null
    override suspend fun getAvailableCountriesFromCache(): ArrayList<Country>? {
        return countries
    }
    override suspend fun saveAvailableCountriesToCache(countries: ArrayList<Country>?) {
        this.countries = countries
    }
}