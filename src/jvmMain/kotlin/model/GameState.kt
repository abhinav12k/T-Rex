package model

import androidx.compose.runtime.mutableStateOf
import java.lang.Integer.max

data class GameState(
    var initialScore: Int = 0,
    var initialHighScore: Int = 0,
    var isGameOver: Boolean = false
) {

    val currentScore = mutableStateOf(initialScore)

    val highScore = mutableStateOf(initialHighScore)

    fun increaseScore() {
        currentScore.value = currentScore.value.inc()
    }

    fun resetGame() {
        highScore.value = max(currentScore.value, highScore.value)
        currentScore.value = 0
        isGameOver = false
    }
}