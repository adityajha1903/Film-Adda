package com.adiandrodev.filmadda.di

import com.adiandrodev.filmadda.data.api.*
import com.adiandrodev.filmadda.data.repositoryimpl.accountstates.datasource.AccountStatesRemoteDataSource
import com.adiandrodev.filmadda.data.repositoryimpl.accountstates.datasourceimpl.AccountStatesRemoteDataSourceImpl
import com.adiandrodev.filmadda.data.repositoryimpl.movie.datasource.MovieRemoteDataSource
import com.adiandrodev.filmadda.data.repositoryimpl.movie.datasourceimpl.MovieRemoteDataSourceImpl
import com.adiandrodev.filmadda.data.repositoryimpl.people.datasource.PeopleRemoteDataSource
import com.adiandrodev.filmadda.data.repositoryimpl.people.datasourceimpl.PeopleRemoteDataSourceImpl
import com.adiandrodev.filmadda.data.repositoryimpl.search.datasource.SearchRemoteDataSource
import com.adiandrodev.filmadda.data.repositoryimpl.search.datasourceimpl.SearchRemoteDataSourceImpl
import com.adiandrodev.filmadda.data.repositoryimpl.trending.datasource.TrendingRemoteDataSource
import com.adiandrodev.filmadda.data.repositoryimpl.trending.datasourceimpl.TrendingRemoteDataSourceImpl
import com.adiandrodev.filmadda.data.repositoryimpl.tv.datasource.TvShowRemoteDataSource
import com.adiandrodev.filmadda.data.repositoryimpl.tv.datasourceimpl.TvShowRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RemoteDataModule(private val apiKey: String) {

    @Singleton
    @Provides
    fun provideApiKey(): String {
        return apiKey
    }

    @Singleton
    @Provides
    fun provideTrendingRemoteDataSource(
        trendingService: TrendingService
    ): TrendingRemoteDataSource {
        return TrendingRemoteDataSourceImpl(
            trendingService,
            apiKey
        )
    }

    @Singleton
    @Provides
    fun provideMovieRemoteDataSource(
        movieService: MovieService
    ): MovieRemoteDataSource {
        return MovieRemoteDataSourceImpl(
            movieService,
            apiKey
        )
    }

    @Singleton
    @Provides
    fun provideTvShowRemoteDataSource(
        tvShowService: TvShowService
    ): TvShowRemoteDataSource {
        return TvShowRemoteDataSourceImpl(
            tvShowService,
            apiKey
        )
    }

    @Singleton
    @Provides
    fun providePeopleRemoteDataSource(
        peopleService: PeopleService
    ): PeopleRemoteDataSource {
        return PeopleRemoteDataSourceImpl(
            peopleService,
            apiKey
        )
    }

    @Singleton
    @Provides
    fun provideAccountStatesRemoteDataSource(
        accountStatesService: AccountStatesService
    ): AccountStatesRemoteDataSource {
        return AccountStatesRemoteDataSourceImpl(
            accountStatesService,
            apiKey
        )
    }

    @Singleton
    @Provides
    fun provideSearchRemoteDataSource(
        searchService: SearchService
    ): SearchRemoteDataSource {
        return SearchRemoteDataSourceImpl(
            searchService,
            apiKey
        )
    }

}
