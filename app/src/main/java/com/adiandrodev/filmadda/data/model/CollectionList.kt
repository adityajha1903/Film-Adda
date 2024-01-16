package com.adiandrodev.filmadda.data.model

import java.io.Serializable

data class CollectionList(
    override val page: Int?,
    override val results: List<CollectionDetails?>?,
    override val total_pages: Int?,
    override val total_results: Int?
): Serializable, AllList
