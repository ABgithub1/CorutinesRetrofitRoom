package com.example.corutinesretrofitroom.retrofit

import com.example.corutinesretrofitroom.data.Person
import retrofit2.http.GET

interface BreakingBadApi {

    @GET("characters")
    suspend fun getPersons(): List<Person>
}


// https://www.breakingbadapi.com/api/characters