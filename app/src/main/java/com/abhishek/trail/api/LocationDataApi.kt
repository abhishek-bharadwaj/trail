package com.abhishek.trail.api

import com.google.gson.GsonBuilder
import io.reactivex.Completable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

object LocationDataApi {

    private val apiService by lazy {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(BasicAuthInterceptor("test/candidate", "c00e-4764"))
            .addInterceptor(interceptor)
            .build()

        Retrofit.Builder().baseUrl("https://api.locus.sh/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }

    fun postLocation(locationPostData: LocationPostObject) =
        apiService.postData(locationPostData)

    interface ApiService {

        @POST("v1/client/test/user/candidate/location")
        fun postData(@Body data: LocationPostObject): Completable
    }
}