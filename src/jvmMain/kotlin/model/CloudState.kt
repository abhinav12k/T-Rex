package model

import androidx.compose.ui.graphics.Path
import cloudPath
import getDeviceWidth
import kotlin.random.Random

data class CloudState(
    val cloudList: MutableList<CloudModel> = mutableListOf(),
    val maxClouds: Int = 3,
    val cloudSpeed: Int = 1
) : GameItemState {

    init {
        initClouds()
    }

    private fun initClouds() {
        var startX = 150
        for (i in 0..maxClouds) {
            val y = rand(0, 100)
            val cloud = CloudModel(startX, y)
            cloudList.add(cloud)
            startX += rand(150, getDeviceWidth())
        }
    }

    override fun moveForward() {
        cloudList.forEach { cloud ->
            cloud.xPos -= cloudSpeed
            if (cloud.xPos < -100) {
                cloud.xPos = rand(getDeviceWidth(), getDeviceWidth() * rand(1, 2))
                cloud.yPos = rand(0, 100)
            }
        }
    }

}

data class CloudModel(
    var xPos: Int = 0,
    var yPos: Int = 0,
    val path: Path = cloudPath
)

fun rand(start: Int, end: Int): Int {
    require(start <= end) { "Illegal Argument" }
    return (Math.random() * (end - start + 1)).toInt() + start
}

fun rand(start: Float, end: Float): Float {
    require(start <= end) { "Illegal Argument" }
    return Random(seed = System.currentTimeMillis()).nextFloat() * (end - start) + start
}