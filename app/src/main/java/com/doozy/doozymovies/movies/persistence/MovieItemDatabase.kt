package com.doozy.doozymovies.movies.persistence

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.doozy.doozymovies.movies.models.MovieItem
import com.doozy.doozymovies.movies.persistence.dao.MovieDao

@Database(entities = [MovieItem::class], version = 1, exportSchema = false)
abstract class MovieItemDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

}
