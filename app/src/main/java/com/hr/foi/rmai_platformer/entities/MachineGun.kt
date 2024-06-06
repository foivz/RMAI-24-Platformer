package com.hr.foi.rmai_platformer.entities

import java.util.concurrent.CopyOnWriteArrayList
import kotlin.math.max
import kotlin.math.min


class MachineGun {
    private val maxBullets = 10
    var numBullets = 0
    var nextBullet = -1
    private var rateOfFire = 1
    private var lastShotTime: Long = -1
    private var bullets = CopyOnWriteArrayList<Bullet>()

    var speed = 25

    fun update(fps: Int, gravity: Float) {
        bullets.forEach { bullet ->
             bullet.update(fps, gravity)
        }
    }

    fun getBulletX(bulletIndex: Int): Float {
        return if (bulletIndex < numBullets) {
            bullets[bulletIndex].x
        }
        else -1f
    }

    fun getBulletY(bulletIndex: Int): Float {
        return if (bulletIndex < numBullets) {
            bullets[bulletIndex].y
        }
        else -1f
    }


    fun hideBullet(index: Int) {
        bullets[index].hideBullet()
    }


    fun getDirection(index: Int): Int {
        return bullets[index].direction
    }


    fun shoot(ownerX: Float, ownerY: Float, ownerFacing: Int, ownerHeight: Float): Boolean {
        var shotFired = false
        if (System.currentTimeMillis() - lastShotTime > 1000 / rateOfFire) {
            nextBullet++

            numBullets = min(maxBullets, numBullets)
            nextBullet = max(maxBullets, 0)

            if (nextBullet == 0) bullets.clear()

            lastShotTime = System.currentTimeMillis()
            bullets.add(Bullet(ownerX, ownerY + ownerHeight / 3, speed, ownerFacing))
            shotFired = true
            numBullets++
        }
        return shotFired
    }


    fun upgradeRateOfFire() {
        rateOfFire += 2
    }

}