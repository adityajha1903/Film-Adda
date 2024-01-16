package com.adiandrodev.filmadda.di

import com.adiandrodev.filmadda.data.api.AccountActionsService
import com.adiandrodev.filmadda.data.api.AuthService
import com.adiandrodev.filmadda.data.db.SearchHistoryDatabase
import com.adiandrodev.filmadda.data.repositoryimpl.accountactions.AccountActionsRepositoryImpl
import com.adiandrodev.filmadda.data.repositoryimpl.accountstates.AccountStatesRepositoryImpl
import com.adiandrodev.filmadda.data.repositoryimpl.accountstates.datasource.AccountStatesCacheDataSource
import com.adiandrodev.filmadda.data.repositoryimpl.accountstates.datasource.AccountStatesRemoteDataSource
import com.adiandrodev.filmadda.data.repositoryimpl.auth.AuthRepositoryImpl
import com.adiandrodev.filmadda.data.repositoryimpl.movie.CollectionDetailsRepositoryImpl
import com.adiandrodev.filmadda.data.repositoryimpl.movie.MovieDetailsRepositoryImpl
import com.adiandrodev.filmadda.data.repositoryimpl.movie.MoviesListsRepositoryImpl
import com.adiandrodev.filmadda.data.repositoryimpl.movie.datasource.MovieCacheDataSource
import com.adiandrodev.filmadda.data.repositoryimpl.movie.datasource.MovieRemoteDataSource
import com.adiandrodev.filmadda.data.repositoryimpl.people.PeopleListRepositoryImpl
import com.adiandrodev.filmadda.data.repositoryimpl.people.PersonDetailsRepositoryImpl
import com.adiandrodev.filmadda.data.repositoryimpl.people.datasource.PeopleCacheDataSource
import com.adiandrodev.filmadda.data.repositoryimpl.people.datasource.PeopleRemoteDataSource
import com.adiandrodev.filmadda.data.repositoryimpl.search.SearchRepositoryImpl
import com.adiandrodev.filmadda.data.repositoryimpl.search.datasource.SearchCacheDataSource
import com.adiandrodev.filmadda.data.repositoryimpl.search.datasource.SearchRemoteDataSource
import com.adiandrodev.filmadda.data.repositoryimpl.searchhistory.SearchHistoryRepositoryImpl
import com.adiandrodev.filmadda.data.repositoryimpl.trending.TrendingRepositoryImpl
import com.adiandrodev.filmadda.data.repositoryimpl.trending.datasource.TrendingCacheDataSource
import com.adiandrodev.filmadda.data.repositoryimpl.trending.datasource.TrendingRemoteDataSource
import com.adiandrodev.filmadda.data.repositoryimpl.tv.TvShowDetailsRepositoryImpl
import com.adiandrodev.filmadda.data.repositoryimpl.tv.TvShowSeasonDetailsRepositoryImpl
import com.adiandrodev.filmadda.data.repositoryimpl.tv.TvShowsListsRepositoryImpl
import com.adiandrodev.filmadda.data.repositoryimpl.tv.datasource.TvShowCacheDataSource
import com.adiandrodev.filmadda.data.repositoryimpl.tv.datasource.TvShowRemoteDataSource
import com.adiandrodev.filmadda.domain.repository.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideTrendingRepository(
        trendingCacheDataSource: TrendingCacheDataSource,
        trendingRemoteDataSource: TrendingRemoteDataSource
    ): TrendingRepository {
        return TrendingRepositoryImpl(
            trendingCacheDataSource,
            trendingRemoteDataSource
        )
    }


    @Provides
    @Singleton
    fun provideMoviesListsRepository(
        movieCacheDataSource: MovieCacheDataSource,
        movieRemoteDataSource: MovieRemoteDataSource
    ): MoviesListsRepository {
        return MoviesListsRepositoryImpl(
            movieCacheDataSource,
            movieRemoteDataSource
        )
    }

    @Provides
    @Singleton
    fun provideTvShowsListsRepository(
        tvShowsCacheDataSource: TvShowCacheDataSource,
        tvShowsRemoteDataSource: TvShowRemoteDataSource
    ): TvShowsListsRepository {
        return TvShowsListsRepositoryImpl(
            tvShowsCacheDataSource,
            tvShowsRemoteDataSource
        )
    }

    @Provides
    @Singleton
    fun providePeopleListsRepository(
        peopleCacheDataSource: PeopleCacheDataSource,
        peopleRemoteDataSource: PeopleRemoteDataSource
    ): PeopleListRepository {
        return PeopleListRepositoryImpl(
            peopleCacheDataSource,
            peopleRemoteDataSource
        )
    }

    @Provides
    @Singleton
    fun providePersonDetailsRepository(
        peopleCacheDataSource: PeopleCacheDataSource,
        peopleRemoteDataSource: PeopleRemoteDataSource
    ): PersonDetailsRepository {
        return PersonDetailsRepositoryImpl(
            peopleCacheDataSource,
            peopleRemoteDataSource
        )
    }

    @Provides
    @Singleton
    fun provideAccountActionsRepository(
        accountActionsService: AccountActionsService,
        apiKey: String
    ): AccountActionsRepository {
        return AccountActionsRepositoryImpl(
            accountActionsService,
            apiKey
        )
    }

    @Provides
    @Singleton
    fun provideAccountStatesRepository(
        accountStatesCacheDataSource: AccountStatesCacheDataSource,
        accountStatesRemoteDataSource: AccountStatesRemoteDataSource
    ): AccountStatesRepository {
        return AccountStatesRepositoryImpl(
            accountStatesCacheDataSource,
            accountStatesRemoteDataSource
        )
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        authService: AuthService,
        apiKey: String
    ): AuthRepository {
        return AuthRepositoryImpl(
            authService,
            apiKey
        )
    }

    @Provides
    @Singleton
    fun provideSearchRepository(
        searchCacheDataSource: SearchCacheDataSource,
        searchRemoteDataSource: SearchRemoteDataSource
    ): SearchRepository {
        return SearchRepositoryImpl(
            searchCacheDataSource,
            searchRemoteDataSource
        )
    }

    @Provides
    @Singleton
    fun provideSearchHistoryRepository(
        searchHistoryDatabase: SearchHistoryDatabase
    ): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(searchHistoryDatabase)
    }

    @Provides
    @Singleton
    fun provideMovieDetailsRepository(
        movieCacheDataSource: MovieCacheDataSource,
        movieRemoteDataSource: MovieRemoteDataSource
    ): MovieDetailsRepository {
        return MovieDetailsRepositoryImpl(
            movieCacheDataSource,
            movieRemoteDataSource
        )
    }

    @Provides
    @Singleton
    fun provideTvShowDetailsRepository(
        tvShowCacheDataSource: TvShowCacheDataSource,
        tvShowRemoteDataSource: TvShowRemoteDataSource
    ): TvShowDetailsRepository {
        return TvShowDetailsRepositoryImpl(
            tvShowCacheDataSource,
            tvShowRemoteDataSource
        )
    }

    @Provides
    @Singleton
    fun provideCollectionRepository(
        movieCacheDataSource: MovieCacheDataSource,
        movieRemoteDataSource: MovieRemoteDataSource
    ): CollectionDetailsRepository {
        return CollectionDetailsRepositoryImpl(
            movieCacheDataSource,
            movieRemoteDataSource
        )
    }

    @Provides
    @Singleton
    fun provideTvShowSeasonDetailsRepository(
        tvShowCacheDataSource: TvShowCacheDataSource,
        tvShowRemoteDataSource: TvShowRemoteDataSource
    ): TvShowSeasonDetailsRepository {
        return TvShowSeasonDetailsRepositoryImpl(
            tvShowCacheDataSource,
            tvShowRemoteDataSource
        )
    }

}
