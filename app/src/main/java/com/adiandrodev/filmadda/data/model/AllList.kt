package com.adiandrodev.filmadda.data.model

interface AllList {
    val page: Int?
    val results: List<ResultItem?>?
    val total_pages: Int?
    val total_results: Int?
}

interface ResultItem {
    val backdrop_path: String?
}