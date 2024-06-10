package com.hr.foi.rmai_platformer.ws

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class Perk(val perk: String)

interface UnlocksService {
    @GET("unlocks.php")
    fun getUnlocksForUser(@Query("username") username: String): Call<List<Perk>>
}