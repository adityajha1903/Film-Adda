package com.adiandrodev.filmadda.data.repositoryimpl.search.datasourceimpl

import com.adiandrodev.filmadda.data.api.SearchService
import com.adiandrodev.filmadda.data.model.CollectionList
import com.adiandrodev.filmadda.data.model.MediaList
import com.adiandrodev.filmadda.data.model.PeopleList
import com.adiandrodev.filmadda.data.repositoryimpl.search.datasource.SearchRemoteDataSource
import retrofit2.Response

class SearchRemoteDataSourceImpl(
    private val searchService: SearchService,
    private val apiKey: String
): SearchRemoteDataSource {

    override suspend fun searchCollection(query: String, region: String): Response<CollectionList> {
        return searchService.searchCollection(query = query, region = region, apiKey = apiKey)
    }

    override suspend fun searchMovies(query: String, region: String): Response<MediaList> {
        return searchService.searchMovie(query = query, region = region, apiKey = apiKey)
    }

    override suspend fun searchTvShows(query: String): Response<MediaList> {
        return searchService.searchTvShows(query = query, apiKey = apiKey)
    }

    override suspend fun searchPeople(query: String): Response<PeopleList> {
        return searchService.searchPerson(query = query, apiKey = apiKey)
    }

}