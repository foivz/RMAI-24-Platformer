package com.hr.foi.rmai_platformer.levels


data class BackgroundData(
    var bitmapName: String,
    var layer: Int, var startY: Float, var endY: Float,
    var speed: Float, var height: Int
)