package com.adiandrodev.filmadda.data.repositoryimpl.accountstates

import android.util.Log
import com.adiandrodev.filmadda.data.model.Country
import com.adiandrodev.filmadda.data.model.MediaList
import com.adiandrodev.filmadda.data.repositoryimpl.accountstates.datasource.AccountStatesCacheDataSource
import com.adiandrodev.filmadda.data.repositoryimpl.accountstates.datasource.AccountStatesRemoteDataSource
import com.adiandrodev.filmadda.domain.model.CombinedAccountStates
import com.adiandrodev.filmadda.domain.repository.AccountStatesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class AccountStatesRepositoryImpl(
    private val accountStatesCacheDataSource: AccountStatesCacheDataSource,
    private val accountStatesRemoteDataSource: AccountStatesRemoteDataSource
): AccountStatesRepository {

    override suspend fun getAllAccountStates(
        accountId: Int,
        sessionId: String
    ): CombinedAccountStates {
        val deferred = CoroutineScope(Dispatchers.IO).async {
            val deferredFavouriteMovies = async(Dispatchers.IO) {
                getFavouriteMovies(accountId, sessionId)
            }
            val deferredFavouriteTvShows = async(Dispatchers.IO) {
                getFavouriteTvShows(accountId, sessionId)
            }
            val deferredRatedMovies = async(Dispatchers.IO) {
                getRatedMovies(accountId, sessionId)
            }
            val deferredRatedTvShows = async(Dispatchers.IO) {
                getRatedTvShows(accountId, sessionId)
            }
            val deferredWatchlistMovies = async(Dispatchers.IO) {
                getMoviesInWatchlist(accountId, sessionId)
            }
            val deferredWatchlistTvShows = async(Dispatchers.IO) {
                getTvShowsInWatchList(accountId, sessionId)
            }
            return@async CombinedAccountStates(
                deferredFavouriteMovies.await(),
                deferredFavouriteTvShows.await(),
                deferredRatedMovies.await(),
                deferredRatedTvShows.await(),
                deferredWatchlistMovies.await(),
                deferredWatchlistTvShows.await()
            )
        }
        return deferred.await()
    }

    private suspend fun getFavouriteMovies(accountId: Int, sessionId: String): MediaList? {
        var movies: MediaList? = null
        try {
            movies = accountStatesCacheDataSource.getFavouriteMoviesFromCache()
        } catch (e: java.lang.Exception) {
            Log.e("TEST_TAG", e.message.toString())
        }
        try {
            if (movies == null) {
                movies = accountStatesRemoteDataSource.getFavouriteMovies(accountId, sessionId).body()
                accountStatesCacheDataSource.saveFavouriteMoviesToCache(movies)
            }
        } catch (e: java.lang.Exception) {
            Log.e("TEST_TAG", e.message.toString())
        }
        return movies
    }

    private suspend fun getFavouriteTvShows(accountId: Int, sessionId: String): MediaList? {
        var tvShows: MediaList? = null
        try {
            tvShows = accountStatesCacheDataSource.getFavouriteTvShowsFromCache()
        } catch (e: java.lang.Exception) {
            Log.e("TEST_TAG", e.message.toString())
        }
        try {
            if (tvShows == null) {
                tvShows = accountStatesRemoteDataSource.getFavouriteTvShows(accountId, sessionId).body()
                accountStatesCacheDataSource.saveFavouriteTvShowsToCache(tvShows)
            }
        } catch (e: java.lang.Exception) {
            Log.e("TEST_TAG", e.message.toString())
        }
        return tvShows
    }

    private suspend fun getRatedMovies(accountId: Int, sessionId: String): MediaList? {
        var movies: MediaList? = null
        try {
            movies = accountStatesCacheDataSource.getRatedMoviesFromCache()
        } catch (e: java.lang.Exception) {
            Log.e("TEST_TAG", "Cache error -> ${e.message.toString()}")
        }
        try {
            if (movies == null) {
                movies = accountStatesRemoteDataSource.getRatedMovies(accountId, sessionId).body()
                accountStatesCacheDataSource.saveRatedMoviesToCache(movies)
            }
        } catch (e: java.lang.Exception) {
            Log.e("TEST_TAG", "Remote error -> ${e.message.toString()}")
        }
        return movies
    }

    private suspend fun getRatedTvShows(accountId: Int, sessionId: String): MediaList? {
        var tvShows: MediaList? = null
        try {
            tvShows = accountStatesCacheDataSource.getRatedTvShowsFromCache()
        } catch (e: java.lang.Exception) {
            Log.e("TEST_TAG", "Cache error -> ${e.message.toString()}")
        }
        try {
            if (tvShows == null) {
                tvShows = accountStatesRemoteDataSource.getRatedTvShows(accountId, sessionId).body()
                accountStatesCacheDataSource.saveRatedTvShowsToCache(tvShows)
            }
        } catch (e: java.lang.Exception) {
            Log.e("TEST_TAG", "Remote error -> ${e.message.toString()}")
        }
        return tvShows
    }

    private suspend fun getMoviesInWatchlist(accountId: Int, sessionId: String): MediaList? {
        var movies: MediaList? = null
        try {
            movies = accountStatesCacheDataSource.getMoviesInWatchlistFromCache()
        } catch (e: java.lang.Exception) {
            Log.e("TEST_TAG", e.message.toString())
        }
        try {
            if (movies == null) {
                movies = accountStatesRemoteDataSource.getMoviesInWatchlist(accountId, sessionId).body()
                accountStatesCacheDataSource.saveMoviesInWatchlistToCache(movies)
            }
        } catch (e: java.lang.Exception) {
            Log.e("TEST_TAG", e.message.toString())
        }
        return movies
    }

    private suspend fun getTvShowsInWatchList(accountId: Int, sessionId: String): MediaList? {
        var tvShows: MediaList? = null
        try {
            tvShows = accountStatesCacheDataSource.getTvShowsInWatchlistFromCache()
        } catch (e: java.lang.Exception) {
            Log.e("TEST_TAG", e.message.toString())
        }
        try {
            if (tvShows == null) {
                tvShows = accountStatesRemoteDataSource.getTvShowsInWatchlist(accountId, sessionId).body()
                accountStatesCacheDataSource.saveTvShowsInWatchlistToCache(tvShows)
            }
        } catch (e: java.lang.Exception) {
            Log.e("TEST_TAG", e.message.toString())
        }
        return tvShows
    }

    override suspend fun getAvailableCountries(): ArrayList<Country> {
        val deferred = CoroutineScope(Dispatchers.IO).async {
            var countries: ArrayList<Country>? = null
            try {
                countries = accountStatesCacheDataSource.getAvailableCountriesFromCache()
            } catch (e: Exception) {
                Log.e("TEST_TAG", e.message.toString())
            }
            try {
                if (countries == null) {
                    countries = accountStatesRemoteDataSource.getAvailableCountries().body()
                    accountStatesCacheDataSource.saveAvailableCountriesToCache(countries)
                }
            } catch (e: Exception) {
                Log.e("TEST_TAG", e.message.toString())
            }
            return@async countries
        }
        return deferred.await() ?: ArrayList()
    }

    override suspend fun clearFavMoviesFromCache() {
        accountStatesCacheDataSource.saveFavouriteMoviesToCache(null)
    }

    override suspend fun clearFavTvShowsFromCache() {
        accountStatesCacheDataSource.saveFavouriteTvShowsToCache(null)
    }

    override suspend fun clearRatedMoviesFromCache() {
        accountStatesCacheDataSource.saveRatedMoviesToCache(null)
    }

    override suspend fun clearRatedTvShowsFromCache() {
        accountStatesCacheDataSource.saveRatedTvShowsToCache(null)
    }

    override suspend fun clearWatchlistMoviesFromCache() {
        accountStatesCacheDataSource.saveMoviesInWatchlistToCache(null)
    }

    override suspend fun clearWatchlistTvShowsFromCache() {
        accountStatesCacheDataSource.saveTvShowsInWatchlistToCache(null)
    }
}