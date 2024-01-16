package com.adiandrodev.filmadda.data.model

import java.io.Serializable

data class PeopleList(
    override val page: Int?,
    override val results: ArrayList<People?>?,
    override val total_pages: Int?,
    override val total_results: Int?
): Serializable, AllList