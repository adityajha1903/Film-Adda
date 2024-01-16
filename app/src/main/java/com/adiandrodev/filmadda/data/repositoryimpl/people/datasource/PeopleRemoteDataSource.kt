package com.adiandrodev.filmadda.data.repositoryimpl.people.datasource

import com.adiandrodev.filmadda.data.model.People
import com.adiandrodev.filmadda.data.model.PeopleList
import retrofit2.Response

interface PeopleRemoteDataSource {

    suspend fun getPopularPeople(page: Int): Response<PeopleList>
    suspend fun getPersonDetails(personId: Int): Response<People>
}