package com.hr.foi.rmai_platformer.entities

class Guard(worldStartX: Int, worldStartY: Int,
            pixelsPerMeter: Int) : GameObject(1f, 2f, 5, "guard", 'g'){
    private var waypointX1 = 0f // Uvijek lijevo
    private var waypointX2 = 0f // Uvijek desno

    private var currentWaypoint = 0
    val MAX_X_VELOCITY = 3f

    init {
        moves = true
        active = true
        visible = true

        animFps = 30
        setAnimated(pixelsPerMeter, true)

        setWorldLocation(worldStartX.toFloat(), worldStartY.toFloat(), 0)
        setxVelocity(-MAX_X_VELOCITY)
        currentWaypoint = 1
    }

    fun setWaypoints(x1: Float, x2: Float) {
        waypointX1 = x1
        waypointX2 = x2
    }

    override fun update(fps: Int, gravity: Float) {
        if (currentWaypoint == 1) { // Ide lijevo
            if (worldLocation.x <= waypointX1) {
                // Stigao na waypoint 1
                currentWaypoint = 2
                setxVelocity(MAX_X_VELOCITY)
                facing = RIGHT
            }
        }
        if (currentWaypoint == 2) {
            if (worldLocation.x >= waypointX2) {
                // Stigao na waypoint 2
                currentWaypoint = 1
                setxVelocity(-MAX_X_VELOCITY)
                facing = LEFT
            }
        }

        move(fps)
        updateRectHitbox()
    }
}