package com.hr.foi.rmai_platformer.utils

class RectHitbox {
    var top = 0f
    var left = 0f
    var bottom = 0f
    var right = 0f
    var height = 0f

    fun intersects(rectHitbox: RectHitbox): Boolean {
        var hit = false

        // Presjek na x-osi
        if (right > rectHitbox.left && left < rectHitbox.right) {
            // Presjek na y-osi
            if (top < rectHitbox.bottom && bottom > rectHitbox.top) {
                // Kolizija
                hit = true
            }
        }
        return hit
    }
}