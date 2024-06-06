package com.hr.foi.rmai_platformer.views

import android.graphics.Rect
import com.hr.foi.rmai_platformer.entities.WorldLocation

class Viewport(screenWidth: Int, screenHeight: Int) {
    private var screenCenterX: Int
    private var screenCenterY: Int
    var pixelsPerMeterX: Int
    var pixelsPerMeterY: Int
    private var metersToShowX: Int
    private var metersToShowY: Int

    private var currentWorldCenter: WorldLocation
    var numClipped: Int = 0

    init {
        screenCenterX = screenWidth / 2
        screenCenterY = screenHeight / 2

        pixelsPerMeterX = screenWidth / 20 // 32
        pixelsPerMeterY = screenHeight / 12 // 24

        metersToShowX = 110
        metersToShowY = 90

        currentWorldCenter = WorldLocation(0f, 0f, 0)
    }

    fun worldToScreen(objectX: Float, objectY: Float, objectWidth: Float, objectHeight: Float): Rect {
        val positionRect = Rect()

        val left = screenCenterX - ((currentWorldCenter.x - objectX) * pixelsPerMeterX)
        val top = screenCenterY - ((currentWorldCenter.y - objectY) * pixelsPerMeterY)
        val right = left + objectWidth * pixelsPerMeterX
        val bottom = top + objectHeight * pixelsPerMeterY

        positionRect.set(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())

        return positionRect
    }

    fun clipObjects(objectX: Float, objectY: Float, objectWidth: Float, objectHeight: Float): Boolean {
        var clipped = true

        if (objectX - objectWidth < currentWorldCenter.x + (metersToShowX / 2)) {
           if (objectX + objectWidth > currentWorldCenter.x - (metersToShowX / 2)) {
               if (objectY - objectHeight < currentWorldCenter.y + (metersToShowY / 2)) {
                   if (objectY + objectHeight > currentWorldCenter.y - (metersToShowY / 2)) {
                       clipped = false
                   }
               }
           }
        }

        if (clipped) {
            numClipped++
        }

        return clipped
    }

    fun resetNumClipped() {
        numClipped = 0
    }

    fun setWorldCenter(x: Float, y: Float) {
        currentWorldCenter.x = x
        currentWorldCenter.y = y
    }
}