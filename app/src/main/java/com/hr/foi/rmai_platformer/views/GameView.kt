package com.hr.foi.rmai_platformer.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.view.MotionEvent
import android.view.SurfaceView
import com.hr.foi.rmai_platformer.entities.GameObject
import com.hr.foi.rmai_platformer.levels.LevelManager
import com.hr.foi.rmai_platformer.utils.InputController
import com.hr.foi.rmai_platformer.utils.RectHitbox

class GameView(context: Context, width: Int, height: Int) : SurfaceView(context) {
    private val paint = Paint()
    private val viewport: Viewport = Viewport(screenWidth, screenHeight)
    private lateinit var levelManager: LevelManager
    private val debugging = false
    private lateinit var inputController: InputController
    private var playerState: PlayerState = PlayerState()

    init {
        loadLevel("LevelCave", 1f, 16f)
    }

    private fun loadLevel(level: String, playerX: Float, playerY: Float) {
        levelManager = LevelManager(level, context, viewport.pixelsPerMeterX, playerX, playerY, screenWidth)
        inputController = InputController(screenWidth, screenHeight, levelManager)

        val location = PointF(playerX, playerY)
        playerState.saveLocation(location)

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
        this.fps = fps
        for (gameObject: GameObject in levelManager.gameObjects) {
            if (gameObject.active) {
                if (!viewport.clipObjects(
                        gameObject.worldLocation.x,
                        gameObject.worldLocation.y,
                        gameObject.width,
                        gameObject.height
                    )) {
                    gameObject.visible = true

                    checkCollisionsWithPlayer(gameObject)
                }

                checkBulletCollisions(gameObject)

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
            if (playerState.getLives() <= 0) {
                playerState = PlayerState()
                loadLevel("LevelCave", 5f, 16f)
            }
        }
    }

    private fun checkBulletCollisions(gameObject: GameObject) {
        for (i in 0 until levelManager.player.bfg.numBullets) {
            val r = RectHitbox()
            r.left = levelManager.player.bfg.getBulletX(i)
            r.top = levelManager.player.bfg.getBulletY(i)
            r.right = levelManager.player.bfg.getBulletX(i) + .1f
            r.bottom = levelManager.player.bfg.getBulletY(i) + .1f

            if (gameObject.rectHitbox.intersects(r)) {
                levelManager.player.bfg.hideBullet(i)

                val objectLocation = gameObject.worldLocation
                if (gameObject.type == 'g') {
                    gameObject.setWorldLocation(
                        objectLocation.x +
                                2 * levelManager.player.bfg.getDirection(i),
                                objectLocation.y, objectLocation.z)
                } else if (gameObject.type == 'd') {
                    gameObject.setWorldLocation(-100f, -100f, 0)
                }
            }
        }
    }

    private fun checkCollisionsWithPlayer(gameObject: GameObject) {
        val hit: Int = levelManager.player.checkCollisions(gameObject.rectHitbox)
        if (hit > 0) {
            when (gameObject.type) {
                else -> {
                    if (hit == 1) {
                        levelManager.player.setxVelocity(0f)
                        levelManager.player.isPressingRight = false
                    }
                    if (hit == 2) {
                        levelManager.player.isFalling = false
                    }
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> levelManager.switchPlayingStatus()
        }

        return true
    }
}