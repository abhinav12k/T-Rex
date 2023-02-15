package theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
val EarthColor: Color
    @Composable get() = if (!isSystemInDarkTheme()) Color(0xFF535353) else Color(0xFFACACAC)

val CloudColor: Color
    @Composable get() = if (!isSystemInDarkTheme()) Color(0xFFDBDBDB) else Color(0xFFACACAC)

val DinoColor: Color
    @Composable get() = if (!isSystemInDarkTheme()) Color(0xFF535353) else Color(0xFFACACAC)

val CactusColor: Color
    @Composable get() = if (!isSystemInDarkTheme()) Color(0xFF535353) else Color(0xFFACACAC)

val GameOverColor: Color
    @Composable get() = if (!isSystemInDarkTheme()) Color(0xFF000000) else Color(0xFFFFFFFF)

val CurrentScoreColor: Color
    @Composable get() = if (!isSystemInDarkTheme()) Color(0xFF535353) else Color(0xFFACACAC)

val HighScoreColor: Color
    @Composable get() = if (!isSystemInDarkTheme()) Color(0xFF757575) else Color(0xFF909191)
