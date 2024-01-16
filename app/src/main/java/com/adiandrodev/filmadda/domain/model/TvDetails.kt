package com.adiandrodev.filmadda.domain.model

import com.adiandrodev.filmadda.data.model.Images
import com.adiandrodev.filmadda.data.model.Media
import com.adiandrodev.filmadda.data.model.Videos
import com.adiandrodev.filmadda.data.model.WatchProviders

data class TvDetails(
    val tvShow: Media?,
    val images: Images?,
    val videos: Videos?,
    val watchProviders: WatchProviders?
)
