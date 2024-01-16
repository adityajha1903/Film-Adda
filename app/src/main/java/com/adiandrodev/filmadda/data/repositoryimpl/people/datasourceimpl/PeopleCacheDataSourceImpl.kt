package com.adiandrodev.filmadda.data.repositoryimpl.people.datasourceimpl

import com.adiandrodev.filmadda.data.model.People
import com.adiandrodev.filmadda.data.model.PeopleList
import com.adiandrodev.filmadda.data.repositoryimpl.people.datasource.PeopleCacheDataSource

class PeopleCacheDataSourceImpl: PeopleCacheDataSource {

    private val peopleLists = ArrayList<PeopleList?>()
    override suspend fun getPopularPeopleFromCache(page: Int): PeopleList? {
        return if (peopleLists.size >= page) {
            peopleLists[page - 1]
        } else {
            null
        }
    }
    override suspend fun savePopularPeopleToCache(page: Int, peopleList: PeopleList?) {
        if (peopleLists.size < page) return
        peopleLists[page - 1] = peopleList
    }

    private var _personId: Int? = null
    private var _person: People? = null
    override suspend fun getPersonDetailsFromCache(personId: Int): People? {
        return if (_personId == personId) {
            _person
        } else {
            null
        }
    }
    override suspend fun savePersonDetailsToCache(personId: Int, person: People?) {
        _person = person
        _personId = personId
    }
}