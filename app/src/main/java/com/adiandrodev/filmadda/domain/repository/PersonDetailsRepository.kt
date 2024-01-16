package com.adiandrodev.filmadda.domain.repository

import com.adiandrodev.filmadda.data.model.People

interface PersonDetailsRepository {
    suspend fun getPersonDetails(personId: Int): People?
}