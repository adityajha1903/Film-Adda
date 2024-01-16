package com.adiandrodev.filmadda.data.repositoryimpl.search

import com.adiandrodev.filmadda.data.model.CollectionList
import com.adiandrodev.filmadda.data.model.MediaList
import com.adiandrodev.filmadda.data.model.PeopleList
import com.adiandrodev.filmadda.data.repositoryimpl.search.datasource.SearchCacheDataSource
import com.adiandrodev.filmadda.data.repositoryimpl.search.datasource.SearchRemoteDataSource
import com.adiandrodev.filmadda.domain.model.SearchResult
import com.adiandrodev.filmadda.domain.repository.SearchRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class SearchRepositoryImpl(
    private val searchCacheDataSource: SearchCacheDataSource,
    private val searchRemoteDataSource: SearchRemoteDataSource
): SearchRepository {

    override suspend fun getSearchResult(query: String, region: String): SearchResult {
        val deferred = CoroutineScope(Dispatchers.IO).async {
            val deferredSearchCollections = async(Dispatchers.IO) {
                getCollection(query, region)
            }
            val deferredSearchMovies = async(Dispatchers.IO) {
                getMovies(query, region)
            }
            val deferredSearchTvShow = async(Dispatchers.IO) {
                getTvShows(query)
            }
            val deferredSearchedPeople = async(Dispatchers.IO) {
                getPeople(query)
            }

            return@async SearchResult(
                deferredSearchCollections.await(),
                deferredSearchMovies.await(),
                deferredSearchTvShow.await(),
                deferredSearchedPeople.await()
            )
        }

        return deferred.await()
    }

    private suspend fun getCollection(query: String, region: String): CollectionList? {
        var collections = searchCacheDataSource.getSearchedCollectionsFromCache(query, region)
        if (collections == null) {
            val response = searchRemoteDataSource.searchCollection(query, region)
            collections = response.body()
            collections?.let{
                searchCacheDataSource.saveSearchedCollectionsToCache(query, region, collections)
            }
        }
        return collections
    }

    private suspend fun getMovies(query: String, region: String): MediaList? {
        var movies = searchCacheDataSource.getSearchedMoviesFromCache(query, region)
        if (movies == null) {
            val response = searchRemoteDataSource.searchMovies(query, region)
            movies = response.body()
            movies?.let{
                searchCacheDataSource.saveSearchedMoviesToCache(query, region, movies)
            }
        }
        return movies
    }

    private suspend fun getTvShows(query: String): MediaList? {
        var tvShows = searchCacheDataSource.getSearchedTvShowsFromCache(query)
        if (tvShows == null) {
            val response = searchRemoteDataSource.searchTvShows(query)
            tvShows = response.body()
            tvShows?.let{
                searchCacheDataSource.saveSearchedTvShowsToCache(query, tvShows)
            }
        }
        return tvShows
    }

    private suspend fun getPeople(query: String): PeopleList? {
        var people = searchCacheDataSource.getSearchedPeopleFromCache(query)
        if (people == null) {
            val response = searchRemoteDataSource.searchPeople(query)
            people = response.body()
            people?.let{
                searchCacheDataSource.saveSearchedPeopleToCache(query, people)
            }
        }
        return people
    }
}