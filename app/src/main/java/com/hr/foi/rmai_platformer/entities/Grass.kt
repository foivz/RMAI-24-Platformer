package com.hr.foi.rmai_platformer.entities

class Grass(locationX : Int, locationY: Int) :
    GameObject(1, 1, 1, "turf", '1') {

    init {
        setWorldLocation(locationX.toFloat(), locationY.toFloat(), 0)
        updateRectHitbox()
    }

    override fun update(fps: Int, gravity: Float) {

    }
}