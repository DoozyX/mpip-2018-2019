package com.doozy.doozymovies.movies.repository

import com.doozy.doozymovies.movies.models.OMDbResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface OMDbApiInterface {
    @GET("/")
    fun listMovies(@Query("apikey") apiKey: String, @Query("s") title: String): Call<OMDbResponse>
}