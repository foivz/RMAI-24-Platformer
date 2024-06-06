package com.hr.foi.rmai_platformer

import android.graphics.Canvas
import android.util.Log
import android.view.SurfaceHolder
import com.hr.foi.rmai_platformer.views.GameView

class GameThread(val surfaceHolder: SurfaceHolder, val gameView: GameView) : Thread() {
    var gameRunning = true
    var fps: Int = 0
    private var frameStartTime: Long = 0
    private var frameElapsedTime: Long = 0
    private var targetTime = 16

    override fun run() {
        while (gameRunning) {
            val canvas: Canvas? = surfaceHolder.lockCanvas()

            if (canvas != null) {
                frameStartTime = System.currentTimeMillis();

                gameView.update(fps)
                gameView.draw(canvas)

                surfaceHolder.unlockCanvasAndPost(canvas)

                frameElapsedTime = System.currentTimeMillis() - frameStartTime
                if (frameElapsedTime > 1) {
                    fps = (1000 / frameElapsedTime).toInt()

                    try {
                        if (fps > 60)  sleep(targetTime - frameElapsedTime)
                    }
                    catch (ex: InterruptedException)
                    {
                    }
                }
            }
        }
    }
}