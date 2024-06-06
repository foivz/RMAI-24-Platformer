
import com.hr.foi.rmai_platformer.entities.GameObject


class Coin(worldStartX: Int, worldStartY: Int, type: Char) :
    GameObject(.5f, .5f, 1, "coin", 'c') {

    init {
        setWorldLocation(worldStartX.toFloat(), worldStartY.toFloat(), 0)
        updateRectHitbox()
    }

    override fun update(fps: Int, gravity: Float) {}
}