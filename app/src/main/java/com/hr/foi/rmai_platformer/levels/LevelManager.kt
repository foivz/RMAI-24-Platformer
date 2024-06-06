package com.hr.foi.rmai_platformer.levels

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.hr.foi.rmai_platformer.entities.GameObject
import com.hr.foi.rmai_platformer.entities.Grass
import com.hr.foi.rmai_platformer.entities.Player

import com.hr.foi.rmai_platformer.entities.platforms.Brick
import com.hr.foi.rmai_platformer.entities.platforms.Coal
import com.hr.foi.rmai_platformer.entities.platforms.Concrete
import com.hr.foi.rmai_platformer.entities.platforms.Grass
import com.hr.foi.rmai_platformer.entities.platforms.Scorched
import com.hr.foi.rmai_platformer.entities.platforms.Snow
import com.hr.foi.rmai_platformer.entities.platforms.Stone
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
            'w' -> 14
            'x' -> 15
            'l' -> 16
            'r' -> 17
            's' -> 18
            'm' -> 19
            'z' -> 20
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
                            'w' -> gameObjects.add(Tree(j, i))
                            'x' -> gameObjects.add(Tree2(j, i))
                            'l' -> gameObjects.add(Lampost(j, i))
                            'r' -> gameObjects.add(Stalactite(j, i))
                            's' -> gameObjects.add(Stalagmite(j, i))
                            'm' -> gameObjects.add(Cart(j, i))
                            'z' -> gameObjects.add(Boulders(j, i))

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