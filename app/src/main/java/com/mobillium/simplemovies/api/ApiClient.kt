package com.mobillium.simplemovies.api

import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient(val context: Context) {

    companion object {
        private fun getRetrofitInstance(): Retrofit {
            val builder = OkHttpClient.Builder()
                .readTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS)
                .connectTimeout(3, TimeUnit.SECONDS)
            val okHttpClient = builder.build()
            val gson = GsonBuilder()
                .setLenient()
                .create()
            return Retrofit.Builder()
                .baseUrl(ApiConstant.baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }

        fun getApiService(): ApiService? {
            return getRetrofitInstance().create(ApiService::class.java)
        }

    }

}