package com.hr.foi.rmai_platformer.views

import android.graphics.Rect

class Animation(pixelsPerMeter: Int, var frameWidth: Int, var frameHeight: Int, val frameCount: Int, animFps: Int) {
    private val sourceRect: Rect
    private var currentFrame: Int = 0
    private var frameTick: Long = 0
    private val framePeriod: Int


    init {
        framePeriod = 1000 / animFps
        frameWidth *= pixelsPerMeter
        frameHeight *= pixelsPerMeter
        sourceRect = Rect(0, 0, frameWidth, frameHeight)
    }

    fun getCurrentFrame(
        time: Long,
        xVelocity: Float,
        moves: Boolean
    ) : Rect {
        if (xVelocity != 0f && !moves) {
            if (time > frameTick + framePeriod) {
                frameTick = time
                currentFrame++

                if (currentFrame >= frameCount) {
                    currentFrame = 0
                }
            }
        }

        this.sourceRect.left = currentFrame * frameWidth
        this.sourceRect.right = this.sourceRect.left + frameWidth

        return this.sourceRect
    }
}