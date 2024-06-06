package com.hr.foi.rmai_platformer.entities

import android.graphics.PointF


class Drone(worldStartX: Int, worldStartY: Int) : GameObject(1f,1f, 1, "drone", 'd') {
    private var lastWaypointSetTime: Long = 0
    private var currentWaypoint: PointF
    private val MAX_X_VELOCITY = 3f
    private val MAX_Y_VELOCITY = 3f

    init {
        moves = true
        active = true
        visible = true

        currentWaypoint = PointF()

        setWorldLocation(worldStartX.toFloat(), worldStartY.toFloat(), 0)
        updateRectHitbox()
        facing = RIGHT
    }

    override fun update(fps: Int, gravity: Float) {
        if (currentWaypoint.x > worldLocation.x) {
            setxVelocity(MAX_X_VELOCITY)
        } else if (currentWaypoint.x < worldLocation.x) {
            setxVelocity(-MAX_X_VELOCITY)
        } else {
            setxVelocity(0f)
        }

        if (currentWaypoint.y >= worldLocation.y) {
            setyVelocity(MAX_Y_VELOCITY)
        } else if (currentWaypoint.y < worldLocation.y) {
            setyVelocity(-MAX_Y_VELOCITY)
        } else {
            setyVelocity(0f)
        }

        move(fps)

        updateRectHitbox()
    }

    fun setWaypoint(playerLocation: WorldLocation) {
        if (System.currentTimeMillis() > lastWaypointSetTime + 2000) {
            lastWaypointSetTime = System.currentTimeMillis()
            currentWaypoint.x = playerLocation.x
            currentWaypoint.y = playerLocation.y
        }
    }
}