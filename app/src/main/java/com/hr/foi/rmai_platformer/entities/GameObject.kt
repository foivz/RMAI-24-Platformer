package com.hr.foi.rmai_platformer.entities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import com.hr.foi.rmai_platformer.utils.RectHitbox
import com.hr.foi.rmai_platformer.views.Animation


abstract class GameObject(
    val width: Float,
    val height: Float,
    var animFrameCount: Int,
    private val bitmapName: String,
    val type: Char) {

    val worldLocation: WorldLocation = WorldLocation(0f, 0f, 0)

    var visible: Boolean = false
    var active: Boolean = true
    var traversable: Boolean = false
    val rectHitbox = RectHitbox()

    private var anim: Animation? = null
    var animated = false
    protected var animFps = 1

    init {
        setxVelocity(0f)
        setyVelocity(0f)
    }

    fun updateRectHitbox() {
        rectHitbox.top = worldLocation.y
        rectHitbox.left = worldLocation.x
        rectHitbox.bottom = worldLocation.y + height
        rectHitbox.right = worldLocation.x + width
    }

    var xVelocity: Float
        get() = _xVelocity
        set(value) {
            if(moves) {
                _xVelocity = value
            }
        }

    var yVelocity: Float
        get() = _yVelocity
        set(value) {
            if(moves) {
                _yVelocity = value
            }
        }

    private var _xVelocity = 0f
    private var _yVelocity = 0f
    val LEFT = -1
    val RIGHT = 1
    var facing = 0
    var moves = false

    abstract fun update(fps: Int, gravity: Float)

    fun setAnimated(
        pixelsPerMetre: Int,
        animated: Boolean
    ) {
        this.animated = animated
        anim = Animation(
            pixelsPerMetre,
            width,
            height,
            animFrameCount,
            animFps
        )
    }

    fun getRectToDraw(deltaTime: Long): Rect {
        return anim!!.getCurrentFrame(
            deltaTime,
            xVelocity,
            moves
        )
    }


    fun setWorldLocation(x: Float, y: Float) {

    }

    fun setWorldLocation(x: Float, y: Float, z: Int) {
            worldLocation.x = x
            worldLocation.y = y
            worldLocation.z = z
    }

    fun prepareBitmap(context: Context, pixelsPerMeter: Int): Bitmap {
        val resID = context.resources.getIdentifier(bitmapName, "drawable",
                context.packageName)

        var bitmap = BitmapFactory.decodeResource(context.resources, resID)
        bitmap = Bitmap.createScaledBitmap(bitmap,
                ((width * animFrameCount * pixelsPerMeter).toInt()),
                ((height * pixelsPerMeter).toInt()),
                false)

        return bitmap
    }

    fun move(fps: Int) {
        if(xVelocity != 0f) {
            this.worldLocation.x += xVelocity / fps
        }
        if(yVelocity != 0f) {
            this.worldLocation.y += yVelocity / fps
        }
    }

    fun setxVelocity(xVelocity: Float) {
        if (moves) {
            this.xVelocity = xVelocity
        }
    }

    fun setyVelocity(yVelocity: Float) {
        if (moves) {
            this.yVelocity = yVelocity
        }
    }
}