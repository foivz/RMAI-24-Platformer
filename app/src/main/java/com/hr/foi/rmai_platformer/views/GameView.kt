package com.hr.foi.rmai_platformer.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceView
import com.hr.foi.rmai_platformer.entities.Drone
import com.hr.foi.rmai_platformer.entities.GameObject
import com.hr.foi.rmai_platformer.entities.PlayerState
import com.hr.foi.rmai_platformer.entities.Teleport
import com.hr.foi.rmai_platformer.levels.GameLostListener
import com.hr.foi.rmai_platformer.levels.LevelManager
import com.hr.foi.rmai_platformer.levels.Location
import com.hr.foi.rmai_platformer.utils.InputController
import com.hr.foi.rmai_platformer.utils.RectHitbox
import com.hr.foi.rmai_platformer.ws.NetworkService
import com.hr.foi.rmai_platformer.ws.Perk
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GameView(context: Context, private val screenWidth: Int, private val screenHeight: Int) : SurfaceView(context) {
    private val paint = Paint()
    private val viewport: Viewport = Viewport(screenWidth, screenHeight)
    private lateinit var levelManager: LevelManager
    private val debugging = false
    private lateinit var inputController: InputController
    private var gameLostListener: GameLostListener? = null

    init {
        loadLevel("TestLevel", 1f, 1f)
    }

    private fun loadLevel(level: String, playerX: Float, playerY: Float) {
        levelManager = LevelManager(level, context, viewport.pixelsPerMeterX, playerX, playerY, screenWidth)
        inputController = InputController(screenWidth, screenHeight, levelManager)

        val location = PointF(playerX, playerY)
        PlayerState.saveLocation(location)

        viewport.setWorldCenter(
            levelManager.player.worldLocation.x,
            levelManager.player.worldLocation.y
        )
    }

    private fun drawGameObject(canvas: Canvas, gameObject: GameObject, toScreen2d: Rect) {
        if (gameObject.animated) {
            if (gameObject.facing == 1) {
                // Rotirano
                val flipper = Matrix()
                flipper.preScale(-1f, 1f)
                val r: Rect = gameObject.getRectToDraw(System.currentTimeMillis())
                val b = Bitmap.createBitmap(
                    levelManager.getBitmap(gameObject.type),
                    r.left,
                    r.top,
                    r.width(),
                    r.height(),
                    flipper,
                    true
                )
                canvas.drawBitmap(
                    b, toScreen2d.left.toFloat(), toScreen2d.top.toFloat(),
                    paint
                )

            } else { // Regularni smjer
                canvas.drawBitmap(
                    levelManager.getBitmap(gameObject.type),
                    gameObject.getRectToDraw(System.currentTimeMillis()),
                    toScreen2d, paint)
            }
        } else { // Bez animacije
            canvas.drawBitmap(levelManager.getBitmap(gameObject.type),
                toScreen2d.left.toFloat(),
                toScreen2d.top.toFloat(),
                paint)
        }

        paint.color = Color.argb(255, 255, 255, 255)
        for (i in 0 until levelManager.player.bfg.numBullets) {
            toScreen2d.set(
                viewport.worldToScreen(
                    levelManager.player.bfg.getBulletX(i),
                    levelManager.player.bfg.getBulletY(i),
                    .25f,
                    .05f
                )
            )
            canvas.drawRect(toScreen2d, paint)
        }
    }


    private fun drawHUD(canvas: Canvas) {
        val topSpace: Float = viewport.pixelsPerMeterY / 4f
        val iconSize: Float = viewport.pixelsPerMeterX.toFloat()
        val padding: Float = viewport.pixelsPerMeterX / 5f
        val centring: Float = viewport.pixelsPerMeterY / 6f

        paint.textSize = viewport.pixelsPerMeterY / 2f
        paint.textAlign = Paint.Align.CENTER

        paint.color = Color.argb(100, 0, 0, 0)
        canvas.drawRect(0f, 0f, iconSize * 7.0f, topSpace * 2 + iconSize, paint)

        paint.color = Color.argb(255, 255, 255, 0)
        canvas.drawBitmap(levelManager.getBitmap('e'), 0f, topSpace, paint)

        canvas.drawText(
            "" + PlayerState.getLives(), iconSize * 1 + padding, iconSize - centring, paint
        )

        canvas.drawBitmap(levelManager.getBitmap('c'), iconSize * 2.5f + padding, topSpace, paint)

        canvas.drawText(
            "" + PlayerState.getCredits(), iconSize * 3.5f + padding * 2,
            iconSize - centring, paint
        )

        canvas.drawText(
            "" + PlayerState.mgFireRate, iconSize * 6.0f + padding * 2,
            iconSize - centring, paint
        )
    }

    private fun drawButtons(canvas: Canvas) {
        paint.color = Color.argb(80, 255, 255, 255)
        val buttonsToDraw: ArrayList<Rect> = inputController.getButtons()
        for (rect in buttonsToDraw) {
            val rf = RectF(
                rect.left.toFloat(), rect.top.toFloat(),
                rect.right.toFloat(), rect.bottom.toFloat()
            )
            canvas.drawRoundRect(rf, 15f, 15f, paint)
        }
    }

    private fun drawPauseScreen(canvas: Canvas) {
        if (!this.levelManager.playing) {
            paint.textAlign = Paint.Align.CENTER
            paint.color = Color.argb(255, 255, 255, 255)
            paint.textSize = 120f
            canvas.drawText("Paused", (screenWidth / 2).toFloat(), (screenHeight / 2).toFloat(), paint)
        }
    }

    var fps = 0
    private fun drawDebugInfo(canvas: Canvas) {
        if (debugging) {
            paint.textSize = 48f
            paint.textAlign = Paint.Align.LEFT
            paint.color = Color.argb(255, 255, 255, 255)

            canvas.drawText("Num objects:" +
                    levelManager.gameObjects.size, 10f, 150f, paint)

            canvas.drawText("FPS:" +
                    fps.toString(), 10f, 200f, paint)
            canvas.drawText("PlayerX:" +
                    levelManager.gameObjects[levelManager.playerIndex].worldLocation.x,
                10f, 250f, paint)
            canvas.drawText("PlayerY:" +
                    levelManager.gameObjects[levelManager.playerIndex].worldLocation.y,
                10f, 300f, paint)
            canvas.drawText("Gravity: " +
                    levelManager.gravity,
                10f, 350f, paint)
            canvas.drawText("xVelocity: " +
                    levelManager.gameObjects[levelManager.playerIndex].xVelocity,
                10f, 400f, paint)
            canvas.drawText("yVelocity: " +
                    levelManager.gameObjects[levelManager.playerIndex].yVelocity,
                10f, 450f, paint)

            viewport.resetNumClipped()
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        if (holder.surface.isValid) {
            paint.color = Color.argb(255, 0, 0, 0)
            canvas.drawColor(Color.argb(255, 0, 0, 200))

            drawBackground(canvas, 0, -3)
            drawGameObjects(canvas)
            drawBackground(canvas, 4, 0)
            drawPauseScreen(canvas)
            drawButtons(canvas)
            drawHUD(canvas)
            drawDebugInfo(canvas)
        }
    }

    private fun drawGameObjects(canvas: Canvas) {
        var toScreen2d: Rect
        for (gameObject in levelManager.gameObjects) {
            for (layer in -1..1) {
                if (gameObject.visible && gameObject.worldLocation.z == layer) {
                    toScreen2d = viewport.worldToScreen(
                        gameObject.worldLocation.x,
                        gameObject.worldLocation.y,
                        gameObject.width,
                        gameObject.height
                    )

                    drawGameObject(canvas, gameObject, toScreen2d)
                }
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

                    if (gameObject.type == 'd') {
                        val d = gameObject as Drone
                        d.setWaypoint(levelManager.player.worldLocation)
                    }
                }
            } else {
                gameObject.visible = false
            }
        }

        if (levelManager.playing) {
            viewport.setWorldCenter(
                levelManager.gameObjects[levelManager.playerIndex].worldLocation.x,
                levelManager.gameObjects[levelManager.playerIndex].worldLocation.y)

            if (levelManager.player.worldLocation.x < 0 ||
                levelManager.player.worldLocation.x > levelManager.levelWidth ||
                levelManager.player.worldLocation.y > levelManager.levelHeight) {

                PlayerState.loseLife()
                levelManager.player.setWorldLocation(levelManager.player.worldLocation.x, levelManager.player.worldLocation.y)
                levelManager.player.setxVelocity(0f)
            }

            if (PlayerState.getLives() <= 0) {
                gameLostListener?.onGameLost()

                PlayerState.reset()
                loadLevel("TestLevel", 1f, 1f)
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

    private fun handlePickup(gameObject: GameObject, hit: Int) {
        gameObject.active = false
        gameObject.visible = false

        // Sve osim pogotka u noge
        if (hit != 2) levelManager.player.restorePreviousVelocity()
    }

    private fun handleExtraLife(gameObject: GameObject, hit: Int) {
        handlePickup(gameObject, hit)
        PlayerState.addLife()
    }

    private fun handleCoinPickup(gameObject: GameObject, hit: Int) {
        handlePickup(gameObject, hit)
        PlayerState.gotCredit()
    }

    private fun handleEnemy() {
        PlayerState.loseLife()

        val location = PointF(PlayerState.loadLocation().x, PlayerState.loadLocation().y)
        levelManager.player.setWorldLocation(location.x, location.y, 0)
        levelManager.player.setxVelocity(0f)
    }

    private fun handleTeleport(gameObject: GameObject) {
        val teleport = gameObject as Teleport
        val t: Location = teleport.target
        loadLevel(t.level, t.x, t.y)
    }

    private fun handleFire() {
        PlayerState.loseLife()

        levelManager.player.setWorldLocation(PlayerState.loadLocation().x, PlayerState.loadLocation().y)
        levelManager.player.setxVelocity(0f)
    }

    private fun handleGunUpgrade(gameObject: GameObject, hit: Int) {
        if (PlayerState.hasRateOfFireUpgrade()) {
            handlePickup(gameObject, hit)
            levelManager.player.bfg.upgradeRateOfFire()
        }
    }

    private fun checkCollisionsWithPlayer(gameObject: GameObject) {
        val hit: Int = levelManager.player.checkCollisions(gameObject.rectHitbox)
        if (hit > 0) {
            when (gameObject.type) {
                'c' -> handleCoinPickup(gameObject, hit)
                'e' -> handleExtraLife(gameObject, hit)
                'd' -> handleEnemy()
                'g' -> handleEnemy()
                'f' -> handleFire()
                't' -> handleTeleport(gameObject)
                'u' -> handleGunUpgrade(gameObject, hit)
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

    private fun drawBackground(canvas: Canvas, start: Int, stop: Int) {
        var fromRect1: Rect
        var toRect1: Rect
        var fromRect2: Rect
        var toRect2: Rect

        levelManager.backgrounds.forEach {bg ->
            if (bg.z < start && bg.z > stop) {
                if (!viewport.clipObjects(-1f, bg.y, 1000f, bg.height.toFloat())) {
                    val startY = viewport.getScreenCenterY() - (viewport.getViewportWorldCentreY() - bg.y) * viewport.getPixelsPerMetreY()
                    val endY = viewport.getScreenCenterY() - (viewport.getViewportWorldCentreY() - bg.endY) * viewport.getPixelsPerMetreY()

                    fromRect1 = Rect(0, 0, bg.width - bg.xClip, bg.height)
                    toRect1 = Rect(bg.xClip, startY.toInt(), bg.width, endY.toInt())


                    fromRect2 = Rect(bg.width - bg.xClip, 0, bg.width, bg.height)
                    toRect2 = Rect(0, startY.toInt(), bg.xClip, endY.toInt())

                    if (!bg.reversedFirst) {
                        canvas.drawBitmap(bg.bitmap, fromRect1, toRect1, paint)
                        canvas.drawBitmap(bg.bitmapReversed, fromRect2, toRect2, paint)
                    } else {
                        canvas.drawBitmap(bg.bitmap, fromRect2, toRect2, paint)
                        canvas.drawBitmap(bg.bitmapReversed, fromRect1, toRect1, paint)
                    }

                    bg.xClip -= (levelManager.player.xVelocity / (20 / bg.speed)).toInt()
                    if (bg.xClip >= bg.width) {
                        bg.xClip = 0
                        bg.reversedFirst = !bg.reversedFirst
                    } else if (bg.xClip <= 0) {
                        bg.xClip = bg.width
                        bg.reversedFirst = !bg.reversedFirst
                    }
                }
            }
        }
    }

    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        inputController.handleInput(motionEvent)

        //invalidate();
        return true
    }

    fun setGameLostListener(listener: GameLostListener) {
            this.gameLostListener = listener
    }
}