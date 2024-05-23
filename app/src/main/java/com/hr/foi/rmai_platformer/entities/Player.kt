package com.hr.foi.rmai_platformer.entities

class Player(locationX: Float, locationY: Float) :
    GameObject(1, 2, 5, "player", 'p') {

    var isPressingLeft = false
    var isPressingRight = false
    val MAX_X_VELOCITY = 10f

    var isFalling = false
    private var isJumping = false
    private val jumpTime: Long = 0
    private val maxJumpTime: Long = 700

    init {
        setWorldLocation(locationX, locationY, 0)

        moves = true
        active = true
        visible = true
    }

    override fun update(fps: Int, gravity: Float) {
        checkCurrentMovementDirection()
        checkPlayerDirection()
        handleJumping(gravity)

        move(fps)
    }

    private fun handleJumping(gravity: Float) {
        if (isJumping) {
            val timeJumping = System.currentTimeMillis() - jumpTime
            if (timeJumping < maxJumpTime) {
                if (timeJumping < maxJumpTime / 2) {
                    yVelocity = -gravity
                } else {
                    yVelocity = gravity
                }
            } else {
                isJumping = false
            }
        } else {
            yVelocity = gravity
            isFalling = true
        }
    }

    private fun checkPlayerDirection() {
        if (xVelocity > 0) {
            facing = RIGHT
        } else if (xVelocity < 0) {
            facing = LEFT
        }
    }

    private fun checkCurrentMovementDirection() {
        if (isPressingRight) {
            xVelocity = MAX_X_VELOCITY
        } else if (isPressingLeft) {
            xVelocity = -MAX_X_VELOCITY
        } else {
            xVelocity = 0f
        }
    }
}