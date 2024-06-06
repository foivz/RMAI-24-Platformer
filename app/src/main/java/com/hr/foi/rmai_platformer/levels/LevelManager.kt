package com.hr.foi.rmai_platformer.levels

import android.content.Context
import android.graphics.Bitmap
import com.hr.foi.rmai_platformer.entities.Drone
import com.hr.foi.rmai_platformer.entities.ExtraLife
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
import com.hr.foi.rmai_platformer.entities.scenery.Boulders
import com.hr.foi.rmai_platformer.entities.scenery.Cart
import com.hr.foi.rmai_platformer.entities.scenery.Lampost
import com.hr.foi.rmai_platformer.entities.scenery.Stalactite
import com.hr.foi.rmai_platformer.entities.scenery.Stalagmite
import com.hr.foi.rmai_platformer.entities.scenery.Tree
import com.hr.foi.rmai_platformer.entities.scenery.Tree2
class LevelManager(level: String, context: Context, pixelsPerMeter: Int, playerX: Float, playerY: Float, screenWidth: Int) {
    val gameObjects: ArrayList<GameObject> = ArrayList()
    val bitmaps: Array<Bitmap?> = arrayOfNulls(20)
    private val bitmaps: Array<Bitmap?> = arrayOfNulls(25)

    private var currentLevel: LevelData? = null
    var playing = false
    var playerIndex = 0
    private var currentIndex = 0
    var player: Player
    var gravity = 6f

    var levelHeight = 0
    var levelWidth = 0

    init {
        currentLevel = when (level) {
            "LevelCave" -> LevelCave()
            "LevelCity" -> LevelCity()
            "LevelForest" -> LevelForest()
            "LevelMountain" -> LevelMountain()
            else -> TestLevel()
        }

       player = Player(0f, 0f, pixelsPerMeter)

       loadMapData(context, pixelsPerMeter, playerX, playerY)
       loadBackgrounds(context, pixelsPerMeter, screenWidth)

    }

    fun getBitmap(blockType: Char): Bitmap {
        var index = getBitmapIndex(blockType)

        return bitmaps[index]!!
    }

    private fun getBitmapIndex(blockType: Char): Int {
        return when(blockType) {
            '1' -> 1
            'p' -> 2
            'c' -> 3
            'e' -> 4
            '2' -> 7
            '3' -> 8
            '4' -> 9
            '5' -> 10
            '6' -> 11
            '7' -> 12
            'w' -> 14
            'x' -> 15
            'l' -> 16
            'r' -> 17
            's' -> 18
            'm' -> 19
            'z' -> 20
            'f' -> 21
            else -> 0
        }
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
                            'c' -> gameObjects.add(Coin(j, i ,c))
                            'e' -> gameObjects.add(ExtraLife(j, i, c))
                            'w' -> gameObjects.add(Tree(j, i))
                            'x' -> gameObjects.add(Tree2(j, i))
                            'l' -> gameObjects.add(Lampost(j, i))
                            'r' -> gameObjects.add(Stalactite(j, i))
                            's' -> gameObjects.add(Stalagmite(j, i))
                            'm' -> gameObjects.add(Cart(j, i))
                            'z' -> gameObjects.add(Boulders(j, i))
                            'f' -> gameObjects.add(Fire(j, i, pixelsPerMeter))
                            '2' -> gameObjects.add(Brick(j, i))
                            '3' -> gameObjects.add(Coal(j, i))
                            '4' -> gameObjects.add(Concrete(j, i))
                            '5' -> gameObjects.add(Scorched(j, i))
                            '6' -> gameObjects.add(Snow(j, i))
                            '7' -> gameObjects.add(Stone(j, i))

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
        gravity = if (playing) {
            6f
        } else {
            0f
        }
    }
    private fun loadBackgrounds(
        context: Context,
        pixelsPerMetre: Int, screenWidth: Int
    ) {
        backgrounds = ArrayList()
        for (bgData in currentLevel!!.backgroundDataList) {
            backgrounds.add(
                Background(
                    context,
                    pixelsPerMetre, screenWidth, bgData
                )
            )
        }
    }
}