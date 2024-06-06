package com.hr.foi.rmai_platformer.entities


class Bullet (var x: Float, val y: Float, speed: Int, val direction: Int) {
    private var xVelocity: Float

    init {
        xVelocity = (speed * direction).toFloat()
    }

    fun update(fps: Int, gravity: Float) {
        x += xVelocity / fps
    }

    fun hideBullet() {
        x = -100f
        xVelocity = 0f
    }
}