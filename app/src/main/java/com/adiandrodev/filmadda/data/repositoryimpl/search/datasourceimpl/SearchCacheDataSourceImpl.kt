package com.adiandrodev.filmadda.data.repositoryimpl.search.datasourceimpl

import com.adiandrodev.filmadda.data.model.CollectionList
import com.adiandrodev.filmadda.data.model.MediaList
import com.adiandrodev.filmadda.data.model.PeopleList
import com.adiandrodev.filmadda.data.repositoryimpl.search.datasource.SearchCacheDataSource

class SearchCacheDataSourceImpl: SearchCacheDataSource {

    private var _collectionsQuery: String? = null
    private var _region: String? = null
    private var _collections: CollectionList? = null
    override suspend fun getSearchedCollectionsFromCache(query: String, region: String): CollectionList? {
        return if (_collectionsQuery == query && _region == region) {
            _collections
        } else {
            null
        }
    }
    override suspend fun saveSearchedCollectionsToCache(query: String, region: String, collections: CollectionList) {
        _collectionsQuery = query
        _region = region
        _collections = collections
    }

    private var _moviesQuery: String? = null
    private var _movies: MediaList? = null
    override suspend fun getSearchedMoviesFromCache(query: String, region: String): MediaList? {
        return if (_moviesQuery == query && _region == region) {
            _movies
        } else {
            null
        }
    }
    override suspend fun saveSearchedMoviesToCache(query: String, region: String, movies: MediaList) {
        _moviesQuery = query
        _region = region
        _movies = movies
    }

    private var _tvShowsQuery: String? = null
    private var _tvShows: MediaList? = null
    override suspend fun getSearchedTvShowsFromCache(query: String): MediaList? {
        return if (_tvShowsQuery == query) {
            _tvShows
        } else {
            null
        }
    }
    override suspend fun saveSearchedTvShowsToCache(query: String, tvShows: MediaList) {
        _tvShowsQuery = query
        _tvShows = tvShows
    }

    private var _peopleQuery: String? = null
    private var _people: PeopleList? = null
    override suspend fun getSearchedPeopleFromCache(query: String): PeopleList? {
        return if (_peopleQuery == query) {
            _people
        } else {
            null
        }
    }
    override suspend fun saveSearchedPeopleToCache(query: String, peopleList: PeopleList) {
        _peopleQuery = query
        _people = peopleList
    }
}