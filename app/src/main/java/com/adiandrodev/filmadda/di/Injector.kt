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

interface Injector {

    fun createProfileSubComponent(): ProfileSubComponent
    fun createTrendingSubComponent(): TrendingSubComponent
    fun createPopPeopleSubComponent(): PopPeopleSubComponent
    fun createMediaListsSubComponent(): MediaListsSubComponent
    fun createSettingsSubComponent(): SettingsSubComponent
    fun createSearchSubComponent(): SearchSubComponent
    fun createMovieSubComponent(): MovieSubComponent
    fun createTvShowSubComponent(): TvShowSubComponent
    fun createCollectionSubComponent(): CollectionSubComponent
    fun createSeasonSubComponent(): SeasonSubComponent
    fun createPersonSubComponent(): PersonSubComponent
}