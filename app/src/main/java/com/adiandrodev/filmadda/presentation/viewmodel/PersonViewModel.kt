package com.adiandrodev.filmadda.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.adiandrodev.filmadda.domain.repository.PersonDetailsRepository
import kotlinx.coroutines.Dispatchers


class PersonViewModel(
    private val personDetailsRepository: PersonDetailsRepository
): ViewModel() {

    fun getPersonDetails(personId: Int) = liveData(Dispatchers.Default) {
        val data = personDetailsRepository.getPersonDetails(personId)
        emit(data)
    }
}

class PersonViewModelFactory(
    private val personDetailsRepository: PersonDetailsRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PersonViewModel(personDetailsRepository) as T
    }
}