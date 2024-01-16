package com.adiandrodev.filmadda.data.repositoryimpl.people

import com.adiandrodev.filmadda.data.model.People
import com.adiandrodev.filmadda.data.repositoryimpl.people.datasource.PeopleCacheDataSource
import com.adiandrodev.filmadda.data.repositoryimpl.people.datasource.PeopleRemoteDataSource
import com.adiandrodev.filmadda.domain.repository.PersonDetailsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class PersonDetailsRepositoryImpl(
    private val peopleCacheDataSource: PeopleCacheDataSource,
    private val peopleRemoteDataSource: PeopleRemoteDataSource
): PersonDetailsRepository {

    override suspend fun getPersonDetails(personId: Int): People? {
        val deferred = CoroutineScope(Dispatchers.IO).async {
            var person = peopleCacheDataSource.getPersonDetailsFromCache(personId)
            if (person == null) {
                val response = peopleRemoteDataSource.getPersonDetails(personId)
                person = response.body()
                peopleCacheDataSource.savePersonDetailsToCache(personId, person)
            }
            return@async person
        }
        return deferred.await()
    }
}