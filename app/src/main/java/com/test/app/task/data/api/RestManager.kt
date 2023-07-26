package com.test.app.task.data.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestManager {
    @get:Throws(Throwable::class)
    val API: ApiCall
        get() = try {
            var retrofit: Retrofit? = null
            val httpClient = OkHttpClient.Builder()
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.cache(null)
            httpClient.addInterceptor(logging)
            val gson = GsonBuilder()
                .setLenient()
                .create()
            if (retrofit == null) {
                val apiUrl = BaseUrl
                retrofit = Retrofit.Builder()
                    .baseUrl(apiUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())
                    .build()
            }
            retrofit!!.create(ApiCall::class.java)
        } catch (e: Exception) {
            throw Throwable(e)
        }

    companion object {
        var BaseUrl = "https://live.flashdiner.com/"
        var Authorization = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiIsImp0aSI6IjYxQjhBMzNBMjVEQkFLVkoifQ.eyJpc3MiOiJsaXZlLmZsYXNoZGluZXIuY29tIiwiYXVkIjoibGl2ZS5mbGFzaGRpbmVyLmNvbSIsImp0aSI6IjYxQjhBMzNBMjVEQkFLVkoiLCJpYXQiOjE2ODk5MjMwODIsIm5iZiI6MTY4OTkyMzA4MiwiZXhwIjoxNzIxNDU5MDgyLCJ0b2tlbiI6Im10N25janI1bnM4dWd1bW4ifQ.qkNwVx9GEJi2X1BNl6LTSbNk3BduMf81KarmeNMW2Qs"
    }
}