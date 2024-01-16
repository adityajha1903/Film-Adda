package com.adiandrodev.filmadda.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "searchEntityTable", indices = [Index(value = ["searchedQuery"], unique = true)])
data class SearchHistoryEntity(
    @PrimaryKey()
    @ColumnInfo()
    val searchedQuery: String
)