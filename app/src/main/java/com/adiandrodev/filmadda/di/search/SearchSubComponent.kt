package com.adiandrodev.filmadda.di.search

import com.adiandrodev.filmadda.di.ActivityScope
import com.adiandrodev.filmadda.presentation.activity.SearchActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [SearchModule::class])
interface SearchSubComponent {
    fun inject(searchActivity: SearchActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create(): SearchSubComponent
    }
}