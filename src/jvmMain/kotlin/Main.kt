import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import model.GameState

@Composable
@Preview
fun App() {
    MaterialTheme {
        GameWindow(GameState())
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose T-Rex"
    ) {
        App()
    }
}
