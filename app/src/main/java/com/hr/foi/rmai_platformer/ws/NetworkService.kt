package com.hr.foi.rmai_platformer.ws

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkService {
    private const val BASE_URL = "http://158.180.45.98/"

    private var instance: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val braintreeService: BraintreeService = instance.create(BraintreeService::class.java)
    val leaderboardService: LeaderboardService = instance.create(LeaderboardService::class.java)
    val unlocksService: UnlocksService = instance.create(UnlocksService::class.java)
}