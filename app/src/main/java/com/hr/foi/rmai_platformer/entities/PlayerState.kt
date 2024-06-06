package com.hr.foi.rmai_platformer.entities

import android.graphics.PointF

class PlayerState {
    private var numCredits = 0
    public var mgFireRate = 0
    private var lives = 0
    private var restartX = 0f
    private var restartY = 0f

    init {
        lives = 3
        mgFireRate = 1
        numCredits = 0
    }

    fun saveLocation(location: PointF) {
        restartX = location.x
        restartY = location.y
    }

    fun loadLocation(): PointF {
        return PointF(restartX, restartY)
    }

    fun gotCredit() {
        numCredits++
    }

    fun getCredits(): Int {
        return numCredits
    }

    fun loseLife() {
        lives--
    }

    fun addLife() {
        lives++
    }

    fun resetLives() {
        lives = 3
    }

    fun resetCredits() {
        numCredits = 0
    }

    fun getLives(): Int {
        return lives
    }
}