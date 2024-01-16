package com.adiandrodev.filmadda.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.adiandrodev.filmadda.domain.repository.CollectionDetailsRepository

class CollectionViewModel(
    private val collectionDetailsRepository: CollectionDetailsRepository
): ViewModel() {

    fun getCollectionDetails(collectionId: Int) = liveData {
        val data = collectionDetailsRepository.getCollectionDetails(collectionId)
        emit(data)
    }
}

class CollectionViewModelFactory(
    private val collectionDetailsRepository: CollectionDetailsRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CollectionViewModel(
            collectionDetailsRepository
        ) as T
    }
}