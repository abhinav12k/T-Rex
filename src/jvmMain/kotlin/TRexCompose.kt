import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.*
import theme.*

const val EARTH_Y_POS = 600F
private const val EARTH_GROUND_STROKE_WIDTH = 8f
private const val CLOUDS_SPEED = 1
private const val MAX_CLOUDS = 3
private const val MAX_EARTH_BLOCKS = 2
private const val MAX_CACTUS = 3

const val EARTH_OFFSET = 200
const val EARTH_SPEED = 10
const val CACTUS_SPEED = EARTH_SPEED

var deviceWidthInPixels = 1600.dp
var distanceBetweenCactus = 600.dp

fun getDeviceWidth() = deviceWidthInPixels.value.toInt()
fun getDistanceBetweenCactus() = distanceBetweenCactus.value.toInt()


@Preview
@Composable
fun DinoGameScenePreview() {
    MaterialTheme {
        GameWindow(GameState())
    }
}

@Composable
fun GameWindow(
    gameState: GameState,
) {
    val cloudState by remember {
        mutableStateOf(
            CloudState(
                maxClouds = MAX_CLOUDS, cloudSpeed = CLOUDS_SPEED
            )
        )
    }
    val earthState by remember {
        mutableStateOf(
            EarthState(
                maxEarthBlocks = MAX_EARTH_BLOCKS, earthSpeed = EARTH_SPEED
            )
        )
    }
    val cactusState by remember {
        mutableStateOf(
            CactusState(
                cactusCount = MAX_CACTUS, cactusSpeed = CACTUS_SPEED
            )
        )
    }
    val dinoState by remember { mutableStateOf(DinoState()) }

    val currentScore = gameState.currentScore
    val highScore = gameState.highScore

    val dinoColor = DinoColor
    val earthColor = EarthColor
    val cactusColor = CactusColor
    val cloudColor = CloudColor
    val gameOverColor = GameOverColor
    val currentScoreColor = CurrentScoreColor
    val highScoreColor = HighScoreColor

    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos {
                if (!gameState.isGameOver) {

                    //Game loop
                    gameState.increaseScore()
                    cloudState.moveForward()
                    cactusState.moveForward()
                    earthState.moveForward()
                    dinoState.move()

                    //Collision check
                    cactusState.cactusList.forEach {
                        if (dinoState.getBounds().deflate(DOUBT_FACTOR)
                                .overlaps(it.getBounds().deflate(DOUBT_FACTOR))
                        ) {
                            gameState.isGameOver = true
                            return@forEach
                        }
                    }

                }
            }
        }
    }

    val interactionSource = remember { MutableInteractionSource() }
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable(
            interactionSource = interactionSource,
            indication = null
        ) {
            if (!gameState.isGameOver) {
                dinoState.jump()
            } else {
                cactusState.initCactus()
                dinoState.init()
                gameState.resetGame()
            }
        }
    ) {

        HighScoreTextViews(
            requireNotNull(currentScore.value),
            requireNotNull(highScore.value),
            highScoreColor,
            currentScoreColor
        )
        Canvas(modifier = Modifier.weight(1f)) {
            EarthView(earthState, earthColor)
            CloudsView(cloudState, cloudColor)
            DinoView(dinoState, dinoColor)
            CactusView(cactusState, cactusColor)
        }

    }

    GameOverTextView(
        modifier = Modifier
            .padding(top = 150.dp)
            .fillMaxWidth(),
        gameState.isGameOver,
        gameOverColor
    )

}

private fun DrawScope.DinoView(
    dinoState: DinoState,
    dinoColor: Color
) {
    withTransform({
        translate(
            left = dinoState.xPos,
            top = dinoState.yPos - dinoState.path.getBounds().height
        )
    }) {
        drawPath(
            dinoState.path,
            color = dinoColor,
            style = Fill
        )
    }
}

private fun DrawScope.CloudsView(
    cloudState: CloudState,
    cloudColor: Color
) {
    cloudState.cloudList.forEach {
        withTransform({
            translate(
                left = it.xPos.toFloat(),
                top = it.yPos.toFloat()
            )
        }) {
            drawPath(
                path = it.path,
                color = cloudColor,
                style = Stroke(3f)
            )
        }
    }
}

private fun DrawScope.EarthView(
    earthState: EarthState,
    earthColor: Color
) {

    //Ground Line
    drawLine(
        color = earthColor,
        start = Offset(0f, EARTH_Y_POS),
        end = Offset(x = getDeviceWidth().toFloat(), y = EARTH_Y_POS),
        strokeWidth = EARTH_GROUND_STROKE_WIDTH
    )

    earthState.earthBlockList.forEach { block ->
        drawLine(
            color = earthColor,
            start = Offset(block.xPos, EARTH_Y_POS + 20),
            end = Offset(block.size, EARTH_Y_POS + 20),
            strokeWidth = EARTH_GROUND_STROKE_WIDTH / 5,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 50f), 0f)
        )
        drawLine(
            color = earthColor,
            start = Offset(x = block.xPos, y = EARTH_Y_POS + 30),
            end = Offset(x = block.size, y = EARTH_Y_POS + 30),
            strokeWidth = EARTH_GROUND_STROKE_WIDTH / 5,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 40f), 40f)
        )
    }
}

private fun DrawScope.CactusView(
    cactusState: CactusState,
    cactusColor: Color
) {
    cactusState.cactusList.forEach {
        withTransform({
            translate(
                left = it.xPos.toFloat(),
                top = EARTH_Y_POS - it.path.getBounds().height
            )
        }) {
            drawPath(
                path = it.path,
                color = cactusColor,
                style = Fill
            )
        }
    }
}

@Composable
fun HighScoreTextViews(
    currentScore: Int,
    highScore: Int,
    highScoreColor: Color,
    currentScoreColor: Color
) {
    Spacer(modifier = Modifier.padding(top = 50.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 20.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Text(text = "HI", style = TextStyle(color = highScoreColor))
        Spacer(modifier = Modifier.padding(start = 10.dp))
        Text(
            text = "$highScore".padStart(5, '0'),
            style = TextStyle(color = highScoreColor)
        )
        Spacer(modifier = Modifier.padding(start = 10.dp))
        Text(
            text = "$currentScore".padStart(5, '0'),
            style = TextStyle(color = currentScoreColor)
        )
    }
}

@Composable
fun GameOverTextView(
    modifier: Modifier = Modifier,
    isGameOver: Boolean = true,
    gameOverColor: Color
) {
    Column(modifier = modifier) {
        Text(
            text = if (isGameOver) "GAME OVER" else "",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            letterSpacing = 5.sp,
            style = TextStyle(
                color = gameOverColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
        if (isGameOver) {
            Icon(
                Icons.Rounded.Refresh,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .padding(top = 10.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            )
        }
    }
}
