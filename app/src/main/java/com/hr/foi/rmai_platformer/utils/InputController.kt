package com.hr.foi.rmai_platformer.utils

import android.graphics.Rect
import android.view.MotionEvent
import com.hr.foi.rmai_platformer.levels.LevelManager

class InputController(screenWidth: Int, screenHeight: Int, private val levelManager: LevelManager) {
    private var x: Int = 0
    private var y: Int = 0

    fun handleInput(motionEvent: MotionEvent) {
        val pointerCount = motionEvent.pointerCount
        for (i in 0..<pointerCount) {
            x = motionEvent.getX(i).toInt()
            y = motionEvent.getY(i).toInt()

            if (levelManager.playing) {
                when (motionEvent.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_DOWN -> handleTouchDown()
                    MotionEvent.ACTION_UP -> handleTouchUp()
                    MotionEvent.ACTION_POINTER_DOWN -> handlePointerDown()
                    MotionEvent.ACTION_POINTER_UP -> handlePointerUp()
                }
            } else {
                when (motionEvent.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_DOWN -> {
                        if (pause.contains(x, y)) {
                            levelManager.switchPlayingStatus()
                        }
                    }
                }
            }
        }
    }

    private fun handlePointerUp() {
        if (right.contains(x, y)) {
            levelManager.player.isPressingRight = false
        } else if (left.contains(x, y)) {
            levelManager.player.isPressingLeft = false
        }
    }

    private fun handlePointerDown() {
        handleMovement()
        handlePause()
        handleShooting()
    }

    private fun handleTouchUp() {
        if (right.contains(x, y)) {
            levelManager.player.isPressingRight = false
        } else if (left.contains(x, y)) {
            levelManager.player.isPressingLeft = false
        }
    }

    private fun handleTouchDown() {
        handleMovement()
        handlePause()
        handleShooting()
    }

    private fun handleShooting() {
        if (shoot.contains(x, y)) {
            levelManager.player.pullTrigger()
        }
    }

    private fun handlePause() {
        if (pause.contains(x, y)) {
            levelManager.switchPlayingStatus()
        }
    }

    private fun handleMovement() {
        if (right.contains(x, y)) {
            levelManager.player.isPressingLeft = false
            levelManager.player.isPressingRight = true
        } else if (left.contains(x, y)) {
            levelManager.player.isPressingLeft = true
            levelManager.player.isPressingRight = false
        } else if (jump.contains(x, y)) {
            levelManager.player.startJump()
        }
    }

    private var left: Rect
    private var right: Rect
    private var jump: Rect
    private var shoot: Rect
    private var pause: Rect

    init {
        val buttonWidth = screenWidth / 10
        val buttonHeight = screenHeight / 8
        val buttonPadding = screenWidth / 18

        left = Rect(
            buttonPadding,
            screenHeight - buttonHeight - buttonPadding,
            buttonWidth + buttonPadding,
            screenHeight - buttonPadding
        )
        right = Rect(
            buttonWidth + buttonPadding,
            screenHeight - buttonHeight - buttonPadding,
            buttonWidth + buttonPadding + buttonWidth,
            screenHeight - buttonPadding
        )
        jump = Rect(
            screenWidth - buttonWidth - buttonPadding,
            screenHeight - buttonHeight - buttonPadding - buttonHeight - buttonPadding,
            screenWidth - buttonPadding,
            screenHeight - buttonPadding - buttonHeight - buttonPadding
        )
        shoot = Rect(
            screenWidth - buttonWidth - buttonPadding,
            screenHeight - buttonHeight - buttonPadding,
            screenWidth - buttonPadding,
            screenHeight - buttonPadding
        )
        pause = Rect(
            screenWidth - buttonPadding - buttonWidth,
            buttonPadding,
            screenWidth - buttonPadding,
            buttonPadding + buttonHeight
        )
    }

    fun getButtons(): ArrayList<Rect> {
        val currentButtonList = ArrayList<Rect>()
        currentButtonList.add(left)
        currentButtonList.add(right)
        currentButtonList.add(jump)
        currentButtonList.add(shoot)
        currentButtonList.add(pause)
        return currentButtonList
    }

}