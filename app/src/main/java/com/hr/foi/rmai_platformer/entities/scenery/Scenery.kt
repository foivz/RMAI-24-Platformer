package com.hr.foi.rmai_platformer.entities.scenery

import com.hr.foi.rmai_platformer.entities.GameObject
import kotlin.random.Random

open class Scenery(worldStartX: Int, worldStartY: Int, bitmapName: String, type: Char) :
    GameObject(worldStartX.toFloat(), worldStartX.toFloat(), 1, bitmapName, type) {
    init {
        active = false // Ne provjeravaj kolizije i sl.

        if (Random.nextInt(2) == 0) {
            setWorldLocation(worldStartX.toFloat(), worldStartY.toFloat(), -1)
        } else {
            setWorldLocation(worldStartX.toFloat(), worldStartY.toFloat(), 1)
        }
    }

    override fun update(fps: Int, gravity: Float) {}
}