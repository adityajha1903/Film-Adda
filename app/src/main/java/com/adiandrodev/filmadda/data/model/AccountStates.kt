package com.adiandrodev.filmadda.data.model

import java.io.Serializable

data class AccountStates(
    var favorite: Boolean?,
    var rated: Rated?,
    var watchlist: Boolean?
): Serializable