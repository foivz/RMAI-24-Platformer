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
import com.hr.foi.rmai_platformer.views.GameView

class GameActivity : AppCompatActivity() {
    private lateinit var gameView: GameView
    private var gameThread: GameThread? = null
    private lateinit var surfaceHolder: SurfaceHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val windowMetrics: WindowMetrics =
            WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(this)
        val screenHeight = windowMetrics.bounds.height()
        val screenWidth = windowMetrics.bounds.width()

        gameView = GameView(this, screenWidth, screenHeight)
        surfaceHolder = gameView.holder
        surfaceHolder.addCallback(surfaceCallback)

        setContentView(gameView)
    }

    override fun onResume() {
        super.onResume()

        gameThread?.gameRunning = true
    }

    override fun onPause() {
        super.onPause()

        gameThread?.gameRunning = false
    }

    private val surfaceCallback: SurfaceHolder.Callback = object : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {
            gameThread = GameThread(holder, gameView)
            gameThread?.start()
        }

        override fun surfaceChanged(holder: SurfaceHolder, p1: Int, p2: Int, p3: Int) { }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            gameThread?.gameRunning = false

            try {
                gameThread?.join()
            } catch (_: InterruptedException) {
            }
        }

    }
}