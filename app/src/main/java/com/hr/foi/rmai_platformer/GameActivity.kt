package com.hr.foi.rmai_platformer

import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.window.layout.WindowMetrics
import androidx.window.layout.WindowMetricsCalculator
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.hr.foi.rmai_platformer.levels.GameLostListener
import com.hr.foi.rmai_platformer.views.GameView

class GameActivity : AppCompatActivity(), GameLostListener {
    private lateinit var gameView: GameView
    private var gameThread: GameThread? = null
    private lateinit var surfaceHolder: SurfaceHolder
    private var holder: SurfaceHolder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val windowMetrics: WindowMetrics =
            WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(this)
        val screenHeight = windowMetrics.bounds.height()
        val screenWidth = windowMetrics.bounds.width()

        gameView = GameView(this, screenWidth, screenHeight)
        gameView.setGameLostListener(this)
        surfaceHolder = gameView.holder
        surfaceHolder.addCallback(surfaceCallback)

        setContentView(gameView)
        loadAd()
    }

    private fun startGame() {
        if (holder != null) {
            gameThread = GameThread(holder!!, gameView)
            gameThread?.start()
        }
    }

    override fun onResume() {
        super.onResume()

        gameThread?.gameRunning = true
        startGame()
    }

    private fun pauseGame() {
        gameThread?.gameRunning = false

        try {
            gameThread?.join()
        } catch (_: InterruptedException) {
        }
    }
    override fun onPause() {
        super.onPause()

        pauseGame()
    }

    private val surfaceCallback: SurfaceHolder.Callback = object : SurfaceHolder.Callback {
        override fun surfaceCreated(h: SurfaceHolder) {
            holder = h
            startGame()
        }

        override fun surfaceChanged(holder: SurfaceHolder, p1: Int, p2: Int, p3: Int) { }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            pauseGame()
        }
    }

    private var interstitialAd: InterstitialAd? = null
    private var adIsLoading = false

    fun loadAd() {
        if (adIsLoading || interstitialAd != null) {
            return
        }
        adIsLoading = true
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            this,
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    this@GameActivity.interstitialAd = interstitialAd
                    adIsLoading = false

                    /*interstitialAd.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                this@GameActivity.interstitialAd = null
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                this@GameActivity.interstitialAd = null
                            }

                            override fun onAdShowedFullScreenContent() {                            }
                        }*/
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    interstitialAd = null
                    adIsLoading = false
                }
            })
    }

    private fun showInterstitial() {
        runOnUiThread {
            if (interstitialAd != null) {
                interstitialAd!!.show(this)
            } else {
                loadAd()
            }
        }
    }

    override fun onGameLost() {
        showInterstitial()
    }
}