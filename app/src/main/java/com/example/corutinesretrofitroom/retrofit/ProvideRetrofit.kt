package com.example.corutinesretrofitroom.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ProvideRetrofit {
    val brBadApi by lazy {
        provideRetrofit().create<BreakingBadApi>()
    }

    private fun provideRetrofit(): Retrofit {
        val client = OkHttpClient
            .Builder()
            .build()
        return Retrofit.Builder()
            .baseUrl("https://www.breakingbadapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}