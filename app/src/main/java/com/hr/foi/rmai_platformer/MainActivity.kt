package com.hr.foi.rmai_platformer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.hr.foi.rmai_platformer.entities.PlayerState
import com.hr.foi.rmai_platformer.ws.NetworkService
import com.hr.foi.rmai_platformer.ws.Perk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var btnStartGame: Button
    private lateinit var btnOpenLeaderboard: Button
    private lateinit var btnOpenUnlocks: Button

    private val username = "Lovro"

    private fun getUnlocks(username: String) {
        val call: Call<List<Perk>> = NetworkService.unlocksService.getUnlocksForUser(username)
        call.enqueue(object : Callback<List<Perk>> {
            override fun onResponse(call: Call<List<Perk>>, response: Response<List<Perk>>) {
                response.body()?.let { perks ->
                    btnStartGame.isEnabled = true
                    PlayerState.setPerkList(perks)
                }
            }

            override fun onFailure(call: Call<List<Perk>>, t: Throwable) {
                Log.e("Unlocks", "Request failed: " + t.message)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStartGame = findViewById(R.id.btnStartGame)
        btnStartGame.setOnClickListener {
            val i = Intent(this, GameActivity::class.java)
            startActivity(i)
        }

        btnOpenLeaderboard = findViewById(R.id.btnOpenLeaderboard)
        btnOpenLeaderboard.setOnClickListener {
            val i = Intent(this, LeaderboardActivity::class.java)
            startActivity(i)
        }

        btnOpenUnlocks = findViewById(R.id.btnOpenUpgrades)
        btnOpenUnlocks.setOnClickListener {
            val i = Intent(this, UnlocksActivity::class.java)
            startActivity(i)
        }

        getUnlocks(username)
        initAdMob()
    }

    private fun initAdMob() {
        val backgroundScope = CoroutineScope(Dispatchers.Main)
        backgroundScope.launch {
            MobileAds.initialize(this@MainActivity) {

            }
        }



    }
}