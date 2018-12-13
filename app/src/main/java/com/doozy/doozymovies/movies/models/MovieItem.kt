package com.doozy.doozymovies.movies.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "movieItem")
class MovieItem {
    @PrimaryKey()
    lateinit var imdbID: String

    lateinit var Year: String
    lateinit var Title: String
    lateinit var Poster: String
}