package com.hr.foi.rmai_platformer.entities

import com.hr.foi.rmai_platformer.levels.Location


class Teleport(worldStartX: Int, worldStartY: Int, target: Location) :
    GameObject(2f, 2f, 1, "door", 't') {
    var target: Location

    init {
        this.target = Location(
            target.level,
            target.x, target.y
        )
        setWorldLocation(worldStartX.toFloat(), worldStartY.toFloat(), 0)
        updateRectHitbox()
    }

    override fun update(fps: Int, gravity: Float) {}
}