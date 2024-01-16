package com.adiandrodev.filmadda.di.person

import com.adiandrodev.filmadda.di.ActivityScope
import com.adiandrodev.filmadda.presentation.activity.PersonActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [PersonModule::class])
interface PersonSubComponent {
    fun inject(personActivity: PersonActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create(): PersonSubComponent
    }
}