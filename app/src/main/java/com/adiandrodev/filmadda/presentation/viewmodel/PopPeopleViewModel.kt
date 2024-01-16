package com.adiandrodev.filmadda.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.adiandrodev.filmadda.domain.repository.PeopleListRepository
import kotlinx.coroutines.Dispatchers

class PopPeopleViewModel(
    private val peopleListRepository: PeopleListRepository
): ViewModel() {

    fun getPopularPeople(page: Int) = liveData(Dispatchers.Default) {
        val data = peopleListRepository.getPopularPeople(page)
        emit(data)
    }
}

class PopPeopleViewModelFactory(
    private val peopleListRepository: PeopleListRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PopPeopleViewModel(peopleListRepository) as T
    }
}