package com.adiandrodev.filmadda.data.repositoryimpl.movie.datasource

import com.adiandrodev.filmadda.data.model.*
import retrofit2.Response

interface MovieRemoteDataSource {

    suspend fun getNowPlayingMovies(region: String): Response<MediaList>
    suspend fun getPopularMovies(region: String): Response<MediaList>
    suspend fun getTopRatedMovies(region: String): Response<MediaList>
    suspend fun getUpcomingMovies(region: String): Response<MediaList>
    suspend fun getMovieDetails(movieId: Int): Response<Media>
    suspend fun getMovieWatchProviders(movieId: Int): Response<WatchProviders>
    suspend fun getMovieImages(movieId: Int): Response<Images>
    suspend fun getMovieVideos(movieId: Int): Response<Videos>
    suspend fun getCollectionDetails(collectionId: Int): Response<CollectionDetails>
}