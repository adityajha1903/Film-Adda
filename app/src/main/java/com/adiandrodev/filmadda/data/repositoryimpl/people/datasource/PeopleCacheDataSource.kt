package com.adiandrodev.filmadda.data.repositoryimpl.people.datasource

import com.adiandrodev.filmadda.data.model.People
import com.adiandrodev.filmadda.data.model.PeopleList

interface PeopleCacheDataSource {

    suspend fun getPopularPeopleFromCache(page: Int): PeopleList?
    suspend fun savePopularPeopleToCache(page: Int, peopleList: PeopleList?)
    suspend fun getPersonDetailsFromCache(personId: Int): People?
    suspend fun savePersonDetailsToCache(personId: Int, person: People?)
}