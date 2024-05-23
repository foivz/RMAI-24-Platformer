package com.hr.foi.rmai_platformer.entities

class Player(locationX: Float, locationY: Float) :
    GameObject(2, 1, 5, "player", 'p') {

    init {
        setWorldLocation(locationX, locationY, 0)
    }

    override fun update(fps: Int, gravity: Float) {
        TODO("Not yet implemented")
    }
}