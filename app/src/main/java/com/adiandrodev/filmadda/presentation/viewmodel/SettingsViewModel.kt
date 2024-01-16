package com.adiandrodev.filmadda.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.adiandrodev.filmadda.data.model.Country
import com.adiandrodev.filmadda.domain.repository.AccountStatesRepository
import kotlinx.coroutines.Dispatchers

class SettingsViewModel(
    private val accountStatesRepository: AccountStatesRepository
): ViewModel() {

    fun getAllAvailableCountries() = liveData(Dispatchers.Default) {
        val data = accountStatesRepository.getAvailableCountries()
        emit(data)
    }
}

class SettingsViewModelFactory(
    private val accountStatesRepository: AccountStatesRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(accountStatesRepository) as T
    }
}

class Countries: ArrayList<Country>()