package model

import EARTH_SPEED
import EARTH_Y_POS
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import cactusPath
import getDeviceWidth
import getDistanceBetweenCactus

data class CactusState(
    val cactusList: MutableList<CactusModel> = mutableListOf(),
    val cactusCount: Int = 3,
    val cactusSpeed: Int = EARTH_SPEED
) : GameItemState {

    init {
        initCactus()
    }

    fun initCactus() {
        cactusList.clear()
        var startX = getDeviceWidth() + 150
        for (i in 0..cactusCount) {
            val cactus = CactusModel(startX, EARTH_Y_POS.toInt())
            cactusList.add(cactus)
            startX += getDistanceBetweenCactus()
        }
    }

    override fun moveForward() {
        cactusList.forEach {
            it.xPos -= cactusSpeed
        }

        if (cactusList.first().xPos < -250) {
            cactusList.removeAt(0)
            val cactus = CactusModel(nextCactusX(cactusList.last().xPos), EARTH_Y_POS.toInt())
            cactusList.add(cactus)
        }
    }

    private fun nextCactusX(lastXPos: Int): Int {
        var newXPos =
            lastXPos + getDistanceBetweenCactus() + rand(0, getDistanceBetweenCactus())
        if (newXPos < getDeviceWidth()) {
            newXPos += getDeviceWidth() - newXPos
        }
        return newXPos
    }

}

data class CactusModel(
    var xPos: Int,
    var yPos: Int,
    val path: Path = cactusPath
) {
    fun getBounds(): Rect {
        return Rect(
            left = xPos.toFloat(),
            top = yPos.toFloat() - path.getBounds().height,
            right = xPos + path.getBounds().width,
            bottom = yPos.toFloat()
        )
    }
}