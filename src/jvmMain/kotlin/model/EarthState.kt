package model

import EARTH_OFFSET
import EARTH_Y_POS
import getDeviceWidth

data class EarthState(
    val earthBlockList: MutableList<EarthModel> = mutableListOf(),
    val maxEarthBlocks: Int = 2,
    val earthSpeed: Int = 5
) : GameItemState {

    init {
        initEarthBlocks()
    }

    private fun initEarthBlocks() {
        var startX = -EARTH_OFFSET.toFloat()
        for (i in 0..maxEarthBlocks) {
            val earthModel = EarthModel(
                xPos = startX,
                yPos = EARTH_Y_POS + (20 + i * 10),
                size = getDeviceWidth() + (EARTH_OFFSET * 2).toFloat()
            )
            earthBlockList.add(earthModel)
            startX += earthModel.size
        }
    }

    override fun moveForward() {
        val xPos = earthBlockList.last().xPos + earthBlockList.last().size
        earthBlockList.forEach {
            it.xPos -= earthSpeed
            if ((it.xPos + it.size) < -EARTH_OFFSET) {
                it.xPos = xPos
            }
        }
    }
}

data class EarthModel(
    var xPos: Float,
    var yPos: Float,
    var size: Float = getDeviceWidth() + EARTH_OFFSET.toFloat()
)