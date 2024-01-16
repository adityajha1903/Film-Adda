package com.adiandrodev.filmadda.data.model

import java.io.Serializable

data class Media(
    override val backdrop_path: String?,
    val created_by: List<CreatedBy?>?,
    val first_air_date: String?,
    val genre_ids: List<Int?>?,
    val id: Int?,
    val in_production: Boolean?,
    val last_air_date: String?,
    val last_episode_to_air: Episode?,
    val networks: List<Network?>?,
    val number_of_episodes: Int?,
    val number_of_seasons: Int?,
    val seasons: ArrayList<Season?>?,
    val media_type: String?,
    val name: String?,
    val origin_country: List<String?>?,
    val original_language: String?,
    val spoken_languages: ArrayList<Language?>?,
    val original_name: String?,
    val original_title: String?,
    val overview: String?,
    val poster_path: String?,
    val release_date: String?,
    val title: String?,
    val vote_average: Double?,
    val vote_count: Int?,
    val rating: Double?,
    val production_companies: List<Company?>?,
    val production_countries: List<Country?>?,
    var account_states: AccountStates?,
    val belongs_to_collection: CollectionDetails?,
    val budget: Long?,
    val credits: Credits?,
    val external_ids: ExternalIds?,
    val genres: ArrayList<Genre?>?,
    val homepage: String?,
    val recommendations: MediaList?,
    val revenue: Long?,
    val reviews: Reviews?,
    val runtime: Int?,
    val status: String?,
    val tagline: String?,
    val type: String?,
    val images: Images?,
    val videos: Videos?,
    var watch_providers: WatchProviders?
): Serializable, ResultItem

data class Season(
    val _id: String?,
    val air_date: String?,
    val episode_count: Int?,
    val episodes: ArrayList<Episode?>?,
    val id: Int?,
    val name: String?,
    val overview: String?,
    val poster_path: String?,
    val season_number: Int?,
    val vote_average: Double?,
    val account_states: AccountStates?,
    val credits: Credits?,
    val external_ids: ExternalIds?,
    val images: Images?,
    val videos: Videos?,
    val watch_providers: WatchProviders?
): Serializable

data class Network(
    val logo_path: String?,
    val name: String?
): Serializable

data class Episode(
    val name : String?,
    val overview: String?,
    val air_date: String?,
    val episode_number: Int?,
    val episode_type: String?,
    val runtime: Int?,
    val season_number: Int?,
    val still_path: String?,
    val vote_average: Double?,
): Serializable

data class Language(
    val english_name: String?,
    val iso_639_1: String?,
    val name: String?,
): Serializable