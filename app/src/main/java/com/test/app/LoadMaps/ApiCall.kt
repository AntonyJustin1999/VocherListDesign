package com.test.app.LoadMaps

import com.test.app.LoadMaps.Utils.ReverseGeoCodeApi
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiCall {

    @GET("maps/api/geocode/json?")
    fun reverseGeoCode(@Query("latlng") latlng: String, @Query("key") token: String): Call<ReverseGeoCodeApi>

}