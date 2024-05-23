package com.hr.foi.rmai_platformer.entities

import com.hr.foi.rmai_platformer.utils.RectHitbox

class Player(locationX: Float, locationY: Float) :
    GameObject(1, 2, 5, "player", 'p') {

    var isPressingLeft = false
    var isPressingRight = false
    val MAX_X_VELOCITY = 10f

    var isFalling = false
    private var isJumping = false
    private val jumpTime: Long = 0
    private val maxJumpTime: Long = 700

    private var rectHitboxFeet = RectHitbox()
    private var rectHitboxHead = RectHitbox()
    private var rectHitboxLeft = RectHitbox()
    private var rectHitboxRight = RectHitbox()

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

        val locX = worldLocation.y
        val locY = worldLocation.y
        updateFeetHitBox(locX, locY)
        updateHeadHitBox(locX, locY)
        updateLeftHitBox(locX, locY)
        updateRightHitBox(locX, locY)
    }

    private fun updateFeetHitBox(lx: Float, ly: Float) {
        rectHitboxFeet.top = ly + height * .95f;
        rectHitboxFeet.left = lx + width * .2f;
        rectHitboxFeet.bottom = ly + height * .98f;
        rectHitboxFeet.right = lx + width * .8f;
    }

    private fun updateHeadHitBox(lx: Float, ly: Float) {
        rectHitboxFeet.top = ly;
        rectHitboxFeet.left = lx + width * .2f;
        rectHitboxFeet.bottom = ly + height * .6f;
        rectHitboxFeet.right = lx + width * .8f;
    }

    private fun updateLeftHitBox(lx: Float, ly: Float) {
        rectHitboxFeet.top = ly + height * .2f;
        rectHitboxFeet.left = lx + width * .2f;
        rectHitboxFeet.bottom = ly + height * .8f;
        rectHitboxFeet.right = lx + width * .3f;
    }

    private fun updateRightHitBox(lx: Float, ly: Float) {
        rectHitboxFeet.top = ly + height * .2f;
        rectHitboxFeet.left = lx + width * .8f;
        rectHitboxFeet.bottom = ly + height * .8f;
        rectHitboxFeet.right = lx + width * .7f;
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

    fun checkCollisions(rectHitbox: RectHitbox): Int {
        var collided = 0 // No collision
        // The left
        if (rectHitboxLeft.intersects(rectHitbox)) {
            // Left has collided
            // Move player just to right of current hitbox
            worldLocation.x = rectHitbox.right - width * .2f
            collided = 1
        }
        // The right
        if (rectHitboxRight.intersects(rectHitbox)) {
            // Right has collided
            // Move player just to left of current hitbox
            worldLocation.x = rectHitbox.left - width * .8f

            collided = 1
        }
        // The feet
        if (rectHitboxFeet.intersects(rectHitbox)) {
            // Feet have collided
            // Move feet to just above current hitbox
            worldLocation.y = rectHitbox.top - height
            collided = 2
        }
        // Now the head
        if (rectHitboxHead.intersects(rectHitbox)) {
            // Head has collided. Ouch!
            // Move head to just below current hitbox bottom
            worldLocation.y = rectHitbox.bottom
            collided = 3
        }
        return collided
    }
}