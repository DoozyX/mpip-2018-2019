package com.doozy.pizzamap.repository

import com.doozy.pizzamap.models.GooglePlacesApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlacesApiInterface {
    @GET("/maps/api/place/nearbysearch/json")
    fun listPlaces(
        @Query("location") location: String, @Query("key") key: String = "", @Query("radius") radius: Int = 5000,
        @Query("types") types: String = "restaurant"
    ): Call<GooglePlacesApiResponse>
}