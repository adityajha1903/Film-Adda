package com.adiandrodev.filmadda.domain.repository

import com.adiandrodev.filmadda.data.model.PeopleList

interface PeopleListRepository {

    suspend fun getPopularPeople(page: Int): PeopleList?
}