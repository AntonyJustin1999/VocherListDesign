package com.test.app.task.data.api

import com.test.app.task.data.dataSets.UserListResponseApi
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiCall {
    @GET("api/v1/prepaid-voucher?")
    fun getUserlist(@Header("Authorization") Authorization:String, @Query("page") page: Int, @Query("env") env: String?): Call<UserListResponseApi?>?

}