package com.adiandrodev.filmadda.data.model

import java.io.Serializable

data class MediaList(
    override val page: Int?,
    override val results: ArrayList<Media?>?,
    override val total_pages: Int?,
    override val total_results: Int?
): Serializable, AllList