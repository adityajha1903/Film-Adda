package com.adiandrodev.filmadda.data.repositoryimpl.people

import android.util.Log
import com.adiandrodev.filmadda.data.model.PeopleList
import com.adiandrodev.filmadda.data.repositoryimpl.people.datasource.PeopleCacheDataSource
import com.adiandrodev.filmadda.data.repositoryimpl.people.datasource.PeopleRemoteDataSource
import com.adiandrodev.filmadda.domain.repository.PeopleListRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class PeopleListRepositoryImpl(
    private val peopleCacheDataSource: PeopleCacheDataSource,
    private val peopleRemoteDataSource: PeopleRemoteDataSource
): PeopleListRepository {
    override suspend fun getPopularPeople(page: Int): PeopleList?  {
        val deferred = CoroutineScope(Dispatchers.IO).async {
            var peopleList = peopleCacheDataSource.getPopularPeopleFromCache(page)
            try {
                if (peopleList == null) {
                    val response = peopleRemoteDataSource.getPopularPeople(page)
                    peopleList = response.body()
                    peopleCacheDataSource.savePopularPeopleToCache(page, peopleList)
                }
            } catch (e: java.lang.Exception) {
                Log.e("TEST_TAG", e.message.toString())
            }
            return@async peopleList
        }
        return deferred.await()
    }
}