package com.adiandrodev.filmadda.di.poppeople

import com.adiandrodev.filmadda.di.FragmentScope
import com.adiandrodev.filmadda.domain.repository.PeopleListRepository
import com.adiandrodev.filmadda.presentation.viewmodel.PopPeopleViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class PopPeopleModule {

    @FragmentScope
    @Provides
    fun providePopPeopleViewModelFactory(
        peopleListRepository: PeopleListRepository
    ): PopPeopleViewModelFactory {
        return PopPeopleViewModelFactory(peopleListRepository)
    }
}