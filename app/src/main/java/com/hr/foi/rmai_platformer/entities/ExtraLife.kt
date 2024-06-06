package com.hr.foi.rmai_platformer.entities

class ExtraLife(worldStartX: Int, worldStartY: Int, type: Char) :
    GameObject(1f, 1f, 1, "life", 'e') {

    init {
        setWorldLocation(worldStartX.toFloat(), worldStartY.toFloat(), 0)
        updateRectHitbox()
    }

    override fun update(fps: Int, gravity: Float) {}
}