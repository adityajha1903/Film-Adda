package com.adiandrodev.filmadda.data.repositoryimpl.people.datasourceimpl

import com.adiandrodev.filmadda.data.api.PeopleService
import com.adiandrodev.filmadda.data.model.People
import com.adiandrodev.filmadda.data.model.PeopleList
import com.adiandrodev.filmadda.data.repositoryimpl.people.datasource.PeopleRemoteDataSource
import retrofit2.Response

class PeopleRemoteDataSourceImpl(
    private val peopleService: PeopleService,
    private val apiKey: String
): PeopleRemoteDataSource {
    override suspend fun getPopularPeople(page: Int): Response<PeopleList> {
        return peopleService.getPopularPeople(page = page, apiKey = apiKey)
    }

    override suspend fun getPersonDetails(personId: Int): Response<People> {
        return peopleService.getPersonDetails(personId = personId, apiKey = apiKey)
    }
}