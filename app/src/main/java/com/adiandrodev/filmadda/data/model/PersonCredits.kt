package com.adiandrodev.filmadda.data.model

import java.io.Serializable

data class PersonCredits(
    val cast: ArrayList<CastOrCrewIn?>?,
    val crew: ArrayList<CastOrCrewIn?>?
): Serializable