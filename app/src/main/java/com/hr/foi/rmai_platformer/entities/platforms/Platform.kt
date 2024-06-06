package com.hr.foi.rmai_platformer.entities.platforms

import com.hr.foi.rmai_platformer.entities.GameObject

open class Platform(locationX : Int, locationY: Int, type: Char, bitmapName: String) :
    GameObject(1f, 1f, 1, bitmapName, type) {

    init {
        setWorldLocation(locationX.toFloat(), locationY.toFloat(), 0)
        updateRectHitbox()

        traversable = true
    }

    override fun update(fps: Int, gravity: Float) {
    }
}