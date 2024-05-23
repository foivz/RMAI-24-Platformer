package com.hr.foi.rmai_platformer.entities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

abstract class GameObject(
        val width: Int,
        val height: Int,
        val animFrameCount: Int,
        val bitmapName: String,
        val type: Char
                ) {

        val worldLocation: WorldLocation = WorldLocation(0f, 0f, 0)

    var visible: Boolean = false
    var active: Boolean = true

    var facing = 0
    val LEFT = -1
    var RIGHT = 1

    private var _xVelocity: Float
    private var _yVelocity: Float

    var moves = false
    var xVelocity: Float
        get() = _xVelocity
        set(value) {
            if (moves) {
                _xVelocity = value
            }
        }

    var yVelocity: Float
        get() = _yVelocity
        set(value) {
            if (moves) {
                _yVelocity = value
            }
        }

    init {
        _xVelocity = 0f
        _yVelocity = 0f
    }

    abstract fun update(fps: Int, gravity: Float)

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
                (width * animFrameCount * pixelsPerMeter),
                (height * pixelsPerMeter),
                false)

        return bitmap
    }

    fun move(fps: Int) {
        if (xVelocity != 0f) {
            worldLocation.x += xVelocity / fps
        }

        if (yVelocity != 0f) {
            worldLocation.y += yVelocity / fps
        }
    }
}