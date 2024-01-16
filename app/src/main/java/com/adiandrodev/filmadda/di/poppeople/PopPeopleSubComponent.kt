package com.adiandrodev.filmadda.di.poppeople

import com.adiandrodev.filmadda.di.FragmentScope
import com.adiandrodev.filmadda.presentation.fragment.PeopleFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [PopPeopleModule::class])
interface PopPeopleSubComponent {
    fun inject(peopleFragment: PeopleFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): PopPeopleSubComponent
    }
}