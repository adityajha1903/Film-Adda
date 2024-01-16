package com.adiandrodev.filmadda.domain.repository

import com.adiandrodev.filmadda.data.model.CollectionDetails

interface CollectionDetailsRepository {

    suspend fun getCollectionDetails(collectionId: Int): CollectionDetails?
}