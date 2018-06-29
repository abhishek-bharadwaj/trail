package com.abhishek.trail.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.http.Body
import retrofit2.http.POST

object LocationDataApi {

    private val apiService by lazy {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        Retrofit.Builder().baseUrl("http://ptsv2.com")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }

    fun postLocation(locationPostData: LocationPostData) {

    }

    interface ApiService {

        @POST("/t/8q2o6-1530243977/post")
        fun postData(@Body data: List<LocationPostData>)
    }
}