package com.hr.foi.rmai_platformer.views

import android.graphics.Rect

class Animation(pixelsPerMeter: Int, var frameWidth: Float, var frameHeight: Float, val frameCount: Int, animFps: Int) {
    private val sourceRect: Rect
    private val framePeriod: Int
    private var currentFrame: Int = 0
    private var frameTick: Long = 0

    init {
        framePeriod = 1000 / animFps
        frameWidth *= pixelsPerMeter
        frameHeight *= pixelsPerMeter
        sourceRect = Rect(0, 0, frameWidth.toInt(), frameHeight.toInt())
    }

    fun getCurrentFrame(
        time: Long,
        xVelocity: Float, moves: Boolean
    ): Rect {
        if (xVelocity != 0f || !moves) {
            if (time > frameTick + framePeriod) {
                frameTick = time
                currentFrame++
                if (currentFrame >= frameCount) {
                    currentFrame = 0
                }
            }
        }

        this.sourceRect.left = (currentFrame * frameWidth).toInt()
        this.sourceRect.right = (this.sourceRect.left + frameWidth).toInt()
        return sourceRect
    }
}