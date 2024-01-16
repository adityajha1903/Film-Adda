package com.adiandrodev.filmadda.di.trending

import com.adiandrodev.filmadda.di.FragmentScope
import com.adiandrodev.filmadda.presentation.fragment.TrendingFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [TrendingModule::class])
interface TrendingSubComponent {

    fun inject(fragment: TrendingFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): TrendingSubComponent
    }
}