package com.adiandrodev.filmadda.di

import com.adiandrodev.filmadda.di.collection.CollectionSubComponent
import com.adiandrodev.filmadda.di.medialists.MediaListsSubComponent
import com.adiandrodev.filmadda.di.movie.MovieSubComponent
import com.adiandrodev.filmadda.di.person.PersonSubComponent
import com.adiandrodev.filmadda.di.poppeople.PopPeopleSubComponent
import com.adiandrodev.filmadda.di.trending.TrendingSubComponent
import com.adiandrodev.filmadda.di.profile.ProfileSubComponent
import com.adiandrodev.filmadda.di.search.SearchSubComponent
import com.adiandrodev.filmadda.di.season.SeasonSubComponent
import com.adiandrodev.filmadda.di.setting.SettingsSubComponent
import com.adiandrodev.filmadda.di.tvshow.TvShowSubComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ApplicationModule::class,
    NetModule::class,
    RepositoryModule::class,
    RemoteDataModule::class,
    CacheDataModule::class,
    LocalDatabaseModule::class
])
interface AppComponent {

    fun profileSubComponent(): ProfileSubComponent.Factory
    fun trendingSubComponent(): TrendingSubComponent.Factory
    fun popPeopleSubComponent(): PopPeopleSubComponent.Factory
    fun mediaListsSubComponent(): MediaListsSubComponent.Factory
    fun settingsSubComponent(): SettingsSubComponent.Factory
    fun searchSubComponent(): SearchSubComponent.Factory
    fun movieSubComponent(): MovieSubComponent.Factory
    fun tvShowSubComponent(): TvShowSubComponent.Factory
    fun collectionSubComponent(): CollectionSubComponent.Factory
    fun seasonSubComponent(): SeasonSubComponent.Factory
    fun personSubComponent(): PersonSubComponent.Factory
}