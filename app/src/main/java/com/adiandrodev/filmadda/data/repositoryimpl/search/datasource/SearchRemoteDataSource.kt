package com.adiandrodev.filmadda.data.repositoryimpl.search.datasource

import com.adiandrodev.filmadda.data.model.CollectionList
import com.adiandrodev.filmadda.data.model.MediaList
import com.adiandrodev.filmadda.data.model.PeopleList
import retrofit2.Response

interface SearchRemoteDataSource {

    suspend fun searchCollection(query: String, region: String): Response<CollectionList>
    suspend fun searchMovies(query: String, region: String): Response<MediaList>
    suspend fun searchTvShows(query: String): Response<MediaList>
    suspend fun searchPeople(query: String): Response<PeopleList>
}