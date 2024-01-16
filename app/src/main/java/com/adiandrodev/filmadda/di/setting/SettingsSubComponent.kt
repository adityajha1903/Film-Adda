package com.adiandrodev.filmadda.di.setting

import com.adiandrodev.filmadda.di.ActivityScope
import com.adiandrodev.filmadda.presentation.activity.SettingsActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [SettingsModule::class])
interface SettingsSubComponent {
    fun inject(settingsActivity: SettingsActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create(): SettingsSubComponent
    }
}