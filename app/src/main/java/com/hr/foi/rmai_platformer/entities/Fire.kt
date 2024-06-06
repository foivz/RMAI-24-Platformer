package com.hr.foi.rmai_platformer.entities

class Fire(worldStartX: Int, worldStartY: Int, pixelsPerMetre: Int) :
    GameObject(1f, 1f, 3, "fire", 'f') {
    init {
        moves = false
        active = true
        visible = true

        animFps = 3

        setAnimated(pixelsPerMetre, true)
        setWorldLocation(worldStartX.toFloat(), worldStartY.toFloat(), 0)
        updateRectHitbox()
    }

    override fun update(fps: Int, gravity: Float) {}
}