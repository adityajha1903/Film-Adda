package com.adiandrodev.filmadda.data.model

import java.io.Serializable

data class Country(
    val name: String?,
    val english_name: String?,
    val iso_3166_1: String?,
    val native_name: String?
): Serializable