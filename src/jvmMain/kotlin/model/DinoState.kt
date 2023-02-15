package model

import EARTH_Y_POS
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import dinoPath
import dinoPath2

data class DinoState(
    var xPos: Float = 60f,
    var yPos: Float = EARTH_Y_POS,
    var velocityY: Float = 0f,
    var gravity: Float = 0f,
    var keyframe: Int = 0,
    private var pathList: MutableList<Path> = mutableListOf(),
    var isJumping: Boolean = false
) : GameItemState {

    val path: Path
        get() = if (keyframe <= 5) pathList[0] else pathList[1]

    init {
        // Adding all keyframes
        pathList.add(dinoPath)
        pathList.add(dinoPath2)
    }

    fun init() {
        xPos = 60f
        yPos = EARTH_Y_POS
        velocityY = 0f
        gravity = 0f
        isJumping = false
    }

    override fun move() {
        yPos += velocityY
        velocityY += gravity

        if (yPos > EARTH_Y_POS) {
            yPos = EARTH_Y_POS
            gravity = 0f
            velocityY = 0f
            isJumping = false
        }
        if (!isJumping) {
            // Change keyframe only when dino is running and not jumping
            changeKeyframe()
        }
    }

    private fun changeKeyframe() {
        keyframe++
        if (keyframe == 10)
            keyframe = 0
    }

    override fun jump() {
        if (yPos == EARTH_Y_POS) {
            isJumping = true
            velocityY = -50f
            gravity = 3f
        }
    }

    fun getBounds(): Rect {
        return Rect(
            left = xPos,
            top = yPos - path.getBounds().height,
            right = xPos + path.getBounds().width,
            bottom = yPos
        )
    }
}