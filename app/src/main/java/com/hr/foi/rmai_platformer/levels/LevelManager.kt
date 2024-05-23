package com.hr.foi.rmai_platformer.levels

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.hr.foi.rmai_platformer.entities.GameObject
import com.hr.foi.rmai_platformer.entities.Grass
import com.hr.foi.rmai_platformer.entities.Player

class LevelManager(level: String, context: Context, pixelsPerMeter: Int, playerX: Float, playerY: Float) {
    val gameObjects: ArrayList<GameObject> = ArrayList()
    val bitmaps: Array<Bitmap?> = arrayOfNulls(20)

    private var currentLevel: LevelData? = null
    var playing = false
    var playerIndex = 0
    private var currentIndex = 0
    var player: Player

    var gravity: Float = 6f

    init {
        currentLevel = when (level) {
           "TestLevel" -> TestLevel()
           "LevelCave" -> LevelCave()
            else -> TestLevel()
       }

       player = Player(0f, 0f)

       loadMapData(context, pixelsPerMeter, playerX, playerY)
       //playing = true
    }

    fun getBitmap(blockType: Char): Bitmap {
        var index = getBitmapIndex(blockType)

        return bitmaps[index]!!
    }

    fun getBitmapIndex(blockType: Char): Int {
        var index = 0

        index = when(blockType) {
            '1' -> 1
            'p' -> 2
            else -> 0
        }

        return index
    }

    private fun loadMapData(context: Context, pixelsPerMeter: Int, playerX: Float, playerY: Float) {
        val levelHeight = currentLevel!!.tiles.size
        val levelWidth = currentLevel!!.tiles[0].length

        var c: Char
        for (j in 0..<levelWidth) {
            for (i in 0..<levelHeight) {
                c = currentLevel!!.tiles[i][j]

                if (c != '.') {
                    when (c) {
                        '1' -> gameObjects.add(Grass(j, i))
                        'p' -> {
                            player = Player(playerX, playerY)
                            gameObjects.add(player)
                            playerIndex = currentIndex
                        }
                    }

                    if (bitmaps[getBitmapIndex(c)] == null) {
                        bitmaps[getBitmapIndex(c)] = gameObjects[currentIndex].prepareBitmap(context, pixelsPerMeter)
                    }
                    currentIndex++

                }
            }
        }
    }

    fun switchPlayingStatus() {
        playing = !playing

        if (!playing) {
            gravity = 0f
        } else {
            gravity = 6f
        }
    }
}