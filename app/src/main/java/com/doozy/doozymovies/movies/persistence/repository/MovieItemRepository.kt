package com.doozy.doozymovies.movies.persistence.repository

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Room
import android.content.Context
import android.os.AsyncTask
import com.doozy.doozymovies.movies.models.MovieItem
import com.doozy.doozymovies.movies.persistence.MovieItemDatabase

class MovieItemRepository(context: Context) {

    private val DB_NAME = "movie_items"

    private var movieItemDatabase: MovieItemDatabase

    init {
        movieItemDatabase = Room.databaseBuilder(context, MovieItemDatabase::class.java, DB_NAME).build()
    }

    fun insertItem(item: MovieItem) {
        object : AsyncTask<Void, Void, Void>() {

            override fun doInBackground(vararg voids: Void): Void? {
                movieItemDatabase.movieDao().insert(item)
                return null
            }

        }.execute()
    }

    fun listAll(): LiveData<List<MovieItem>> {
        return movieItemDatabase.movieDao().all
    }

    fun deleteAll() {
        object : AsyncTask<Void, Void, Void>() {

            override fun doInBackground(vararg voids: Void): Void? {
                movieItemDatabase.movieDao().deleteAll()
                return null
            }
        }.execute()
    }

    fun getById(id: String): LiveData<MovieItem> {
        return movieItemDatabase.movieDao().getItemsById(id)
    }

}
