package com.hr.foi.rmai_platformer.entities

import com.hr.foi.rmai_platformer.utils.RectHitbox


class Player(locationX: Float, locationY: Float, pixelsPerMetre: Int) :
    GameObject(1f, 2f, 5, "player", 'p') {

    val MAX_X_VELOCITY = 10f
    var isPressingRight = false
    var isPressingLeft = false
    var isFalling = false
    private var isJumping = false
    private var jumpTime: Long = 0
    private val maxJumpTime: Long = 700 // jump 7 10ths of second

    private var rectHitboxFeet = RectHitbox()
    private var rectHitboxHead = RectHitbox()
    private var rectHitboxLeft = RectHitbox()
    private var rectHitboxRight = RectHitbox()

    val ANIMATION_FPS = 30
    val ANIMATION_FRAME_COUNT = 5

    var bfg: MachineGun = MachineGun()

    init {
        setWorldLocation(locationX, locationY, 0)

        facing = LEFT
        isFalling = false

        moves = true
        active = true
        visible = true

        animFps = ANIMATION_FPS
        animFrameCount = ANIMATION_FRAME_COUNT
        setAnimated(pixelsPerMetre, true)
    }

    override fun update(fps: Int, gravity: Float) {
        checkCurrentMovementDirection()
        checkPlayerDirection()
        handleJumping(gravity)

        move(fps)
        bfg.update(fps, gravity)

        val locX = worldLocation.x
        val locY = worldLocation.y
        updateLeftHitbox(locX, locY)
        updateRightHitbox(locX, locY)
        updateHeadHitbox(locX, locY)
        updateFeetHitBox(locX, locY)
    }

    private fun handleJumping(gravity: Float) {
        if (isJumping) {
            val timeJumping = System.currentTimeMillis() - jumpTime
            if (timeJumping < maxJumpTime) {
                if (timeJumping < maxJumpTime / 2) {
                    setyVelocity(-gravity) //on the way up
                } else if (timeJumping > maxJumpTime / 2) {
                    setyVelocity(gravity) //going down
                }
            } else {
                isJumping = false
            }
        } else {
            setyVelocity(gravity)
            isFalling = true
        }
    }

    private fun checkCurrentMovementDirection() {
        if (isPressingRight) {
            this.setxVelocity(MAX_X_VELOCITY);
        } else if (isPressingLeft) {
            this.setxVelocity(-MAX_X_VELOCITY);
        } else {
            this.setxVelocity(0f);
        }
    }

    private fun checkPlayerDirection() {
        if (xVelocity > 0) {
            facing = RIGHT;
        } else if (xVelocity < 0) {
            facing = LEFT;
        }
    }

    private fun updateFeetHitBox(lx: Float, ly: Float) {
        rectHitboxFeet.top = ly + height * .8f;
        rectHitboxFeet.left = lx + width * .2f;
        rectHitboxFeet.bottom = ly + height * .9f;
        rectHitboxFeet.right = lx + width * .8f;
    }

    private fun updateHeadHitbox(lx: Float, ly: Float) {
        rectHitboxHead.top = ly;
        rectHitboxHead.left = lx + width * .4f;
        rectHitboxHead.bottom = ly + height * .2f;
        rectHitboxHead.right = lx + width * .6f;
    }

    private fun updateLeftHitbox(lx: Float, ly: Float) {
        rectHitboxLeft.top = ly + height * .2f;
        rectHitboxLeft.left = lx + width * .2f;
        rectHitboxLeft.bottom = ly + height * .6f;
        rectHitboxLeft.right = lx + width * .3f;
    }

    private fun updateRightHitbox(lx: Float, ly: Float) {
        rectHitboxRight.top = ly + height * .2f;
        rectHitboxRight.left = lx + width * .8f;
        rectHitboxRight.bottom = ly + height * .6f;
        rectHitboxRight.right = lx + width * .7f;
    }

    fun checkCollisions(rectHitbox: RectHitbox): Int {
        var collided = 0

        if (rectHitboxLeft.intersects(rectHitbox)) {
            worldLocation.x = rectHitbox.right - width * .2f
            collided = 1
        }

        if (rectHitboxRight.intersects(rectHitbox)) {
            worldLocation.x = rectHitbox.left - width * .8f
            collided = 1
        }

        if (rectHitboxFeet.intersects(rectHitbox)) {
            worldLocation.y = rectHitbox.top - height
            collided = 2
        }

        if (rectHitboxHead.intersects(rectHitbox)) {
            worldLocation.y = rectHitbox.bottom
            collided = 3
        }
        return collided
    }

    fun startJump() {
        if (!isFalling && !isJumping) {
                isJumping = true
                jumpTime = System.currentTimeMillis()
        }
    }

    fun restorePreviousVelocity() {
        if (!isJumping && !isFalling) {
            if (facing == LEFT) {
                isPressingLeft = true
                setxVelocity(-MAX_X_VELOCITY)
            } else {
                isPressingRight = true
                setxVelocity(MAX_X_VELOCITY)
            }
        }
    }

    fun pullTrigger(): Boolean {
        return bfg.shoot(worldLocation.x,
            worldLocation.y,
            facing, height);
    }

}