package com.hr.foi.rmai_platformer.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.MotionEvent
import android.view.SurfaceView
import com.hr.foi.rmai_platformer.entities.GameObject
import com.hr.foi.rmai_platformer.levels.LevelManager

class GameView(context: Context, width: Int, height: Int) : SurfaceView(context) {
    private val paint = Paint()
    private val viewport: Viewport
    private lateinit var levelManager: LevelManager

    private val debugging = true

    init {
        viewport = Viewport(width, height)

        loadLevel("TestLevel", 15f, 0.25f)
    }

    fun loadLevel(level: String, playerX: Float, playerY: Float) {
        levelManager = LevelManager(level, context, viewport.pixelsPerMeterX, playerX, playerY)

        viewport.setWorldCenter(
            levelManager.player.worldLocation.x,
            levelManager.player.worldLocation.y
        )
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        if (holder.surface.isValid) {
            canvas.drawColor(Color.argb(255, 0, 0, 200))

            var toScreen2d: Rect
            for (layer in -1..1) {
                for (gameObject in levelManager.gameObjects) {
                    if (gameObject.visible && gameObject.worldLocation.z == layer) {
                        toScreen2d = viewport.worldToScreen(
                            gameObject.worldLocation.x,
                            gameObject.worldLocation.y,
                            gameObject.width,
                            gameObject.height
                        )

                        canvas.drawBitmap(levelManager.getBitmap(gameObject.type),
                            toScreen2d.left.toFloat(),
                            toScreen2d.top.toFloat(),
                            paint)
                    }
                }
            }

            if (debugging) {
                paint.textSize = 48f
                paint.textAlign = Paint.Align.LEFT
                paint.color = Color.argb(255, 255, 255, 255)

                canvas.drawText("Num objects ${levelManager.gameObjects.size}",
                                10f, 50f, paint)
                canvas.drawText("Num clipped: ${viewport.numClipped}",
                                10f, 100f, paint)
                canvas.drawText("PlayerX: " +
                        "${levelManager.gameObjects[levelManager.playerIndex].worldLocation.x}",
                        10f, 150f, paint)
                canvas.drawText("PlayerY: " +
                        "${levelManager.gameObjects[levelManager.playerIndex].worldLocation.y}",
                        10f, 200f, paint)
                canvas.drawText("Gravity: " +
                        levelManager.gravity,
                    10f, 250f, paint)
                canvas.drawText("xVelocity: " +
                        levelManager.gameObjects[levelManager.playerIndex].xVelocity,
                    10f, 300f, paint)
                canvas.drawText("yVelocity: " +
                        levelManager.gameObjects[levelManager.playerIndex].yVelocity,
                    10f, 350f, paint)

                viewport.resetNumClipped()
            }
        }
    }

    fun update(fps: Int) {
        for (gameObject: GameObject in levelManager.gameObjects) {
            if (gameObject.active) {
                if (!viewport.clipObjects(
                        gameObject.worldLocation.x,
                        gameObject.worldLocation.y,
                        gameObject.width.toFloat(),
                        gameObject.height.toFloat())) {
                    gameObject.visible = true
                }

                if (levelManager.playing) {
                    gameObject.update(fps, levelManager.gravity)
                }
            } else {
                gameObject.visible = false
            }
        }

        if (levelManager.playing) {
            viewport.setWorldCenter(
                levelManager.gameObjects[levelManager.playerIndex].worldLocation.x,
                levelManager.gameObjects[levelManager.playerIndex].worldLocation.y
            )
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> levelManager.switchPlayingStatus()
        }

        return true
    }
}