package com.adiandrodev.filmadda.data.repositoryimpl.search.datasource

import com.adiandrodev.filmadda.data.model.CollectionList
import com.adiandrodev.filmadda.data.model.MediaList
import com.adiandrodev.filmadda.data.model.PeopleList

interface SearchCacheDataSource {

    suspend fun getSearchedCollectionsFromCache(query: String, region: String): CollectionList?
    suspend fun saveSearchedCollectionsToCache(query: String, region: String, collections: CollectionList)
    suspend fun getSearchedMoviesFromCache(query: String, region: String): MediaList?
    suspend fun saveSearchedMoviesToCache(query: String, region: String, movies: MediaList)
    suspend fun getSearchedTvShowsFromCache(query: String): MediaList?
    suspend fun saveSearchedTvShowsToCache(query: String, tvShows: MediaList)
    suspend fun getSearchedPeopleFromCache(query: String): PeopleList?
    suspend fun saveSearchedPeopleToCache(query: String, peopleList: PeopleList)

}