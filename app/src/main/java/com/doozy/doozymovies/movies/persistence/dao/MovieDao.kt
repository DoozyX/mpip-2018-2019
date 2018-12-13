package com.doozy.doozymovies.movies.persistence.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.doozy.doozymovies.movies.models.MovieItem

@Dao
interface MovieDao {

    @get:Deprecated("")
    @get:Query("SELECT * FROM movieItem c ORDER BY c.title")
    val allSync: List<MovieItem>

    @get:Query("SELECT * FROM movieItem c ORDER BY c.title")
    val all: LiveData<List<MovieItem>>

    @Insert
    fun insert(item: MovieItem)

    @Update
    fun update(item: MovieItem)

    @Delete
    fun delete(item: MovieItem)

    @Query("DELETE from movieItem")
    fun deleteAll()

    @Query("SELECT * FROM movieItem where title=:title")
    fun getItemsByTitle(title: String): LiveData<List<MovieItem>>

    @Query("SELECT * FROM movieItem where imdbID=:id")
    fun getItemsById(id: String): LiveData<MovieItem>
}
