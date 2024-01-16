package com.adiandrodev.filmadda.data.repositoryimpl.movie

import com.adiandrodev.filmadda.data.model.MediaList
import com.adiandrodev.filmadda.data.repositoryimpl.movie.datasource.MovieCacheDataSource
import com.adiandrodev.filmadda.data.repositoryimpl.movie.datasource.MovieRemoteDataSource
import com.adiandrodev.filmadda.domain.model.MoviesLists
import com.adiandrodev.filmadda.domain.repository.MoviesListsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class MoviesListsRepositoryImpl(
    private val movieCacheDataSource: MovieCacheDataSource,
    private val movieRemoteDataSource: MovieRemoteDataSource
): MoviesListsRepository {

    override suspend fun getMoviesLists(region: String): MoviesLists? {
        val deferred = CoroutineScope(Dispatchers.IO).async {
            val deferredNowPlayingMovies = async(Dispatchers.IO) {
                getNowPlayingMovies(region)
            }
            val deferredPopularMovies = async(Dispatchers.IO) {
                getPopularMovies(region)
            }
            val deferredTopRatedMovies = async(Dispatchers.IO) {
                getTopRatedMovies(region)
            }
            val deferredUpcomingMovies = async(Dispatchers.IO) {
                getUpcomingMovies(region)
            }
            val lists = MoviesLists(
                deferredNowPlayingMovies.await(),
                deferredPopularMovies.await(),
                deferredTopRatedMovies.await(),
                deferredUpcomingMovies.await()
            )
            if (
                lists.nowPlaying == null
                && lists.popular == null
                && lists.topRated == null
                && lists.upcoming == null
            ) {
                return@async null
            } else {
                return@async lists
            }
        }
        return deferred.await()
    }

    private suspend fun getNowPlayingMovies(region: String): MediaList? {
        var movies = movieCacheDataSource.getNowPlayingMoviesFromCache(region)
        if (movies == null) {
            val response = movieRemoteDataSource.getNowPlayingMovies(region)
            movies = response.body()
            movieCacheDataSource.saveNowPlayingMoviesToCache(region, movies)
        }
        return movies
    }

    private suspend fun getPopularMovies(region: String): MediaList? {
        var movies = movieCacheDataSource.getPopularMoviesFromCache(region)
        if (movies == null) {
            val response = movieRemoteDataSource.getPopularMovies(region)
            movies = response.body()
            movieCacheDataSource.savePopularMoviesToCache(region, movies)
        }
        return movies
    }

    private suspend fun getTopRatedMovies(region: String): MediaList? {
        var movies = movieCacheDataSource.getTopRatedMoviesFromCache(region)
        if (movies == null) {
            val response = movieRemoteDataSource.getTopRatedMovies(region)
            movies = response.body()
            movieCacheDataSource.saveTopRatedMoviesToCache(region, movies)
        }
        return movies
    }

    private suspend fun getUpcomingMovies(region: String): MediaList? {
        var movies = movieCacheDataSource.getUpcomingMoviesFromCache(region)
        if (movies == null) {
            val response = movieRemoteDataSource.getUpcomingMovies(region)
            movies = response.body()
            movieCacheDataSource.saveUpcomingMoviesToCache(region, movies)
        }
        return movies
    }
}