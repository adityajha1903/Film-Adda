package com.adiandrodev.filmadda.data.model

import java.io.Serializable

data class Credits(
    val cast: List<People?>?,
    val crew: List<People?>?
): Serializable