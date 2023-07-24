package com.test.app.loadmaps.data.api

import com.test.app.loadmaps.data.dataSets.CountriesApi
import com.test.app.loadmaps.data.dataSets.CountryDetailsApi
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiCall {
    @GET("v3.1/independent?")
    fun getAllCountrieslist(@Query("status") status: Boolean, @Query("fields") fields: String?): Call<ArrayList<CountriesApi?>?>?

    @GET("v3.1/name/{countryName}")
    fun getCountryInfo(@Path("countryName") countryName: String?): Call<ArrayList<CountryDetailsApi?>?>?
}