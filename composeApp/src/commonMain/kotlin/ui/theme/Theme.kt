package ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import data.value.Theme

private val DarkColorScheme = darkColorScheme(
    primary = Orange80,
    onPrimary = OrangeGrey80,
    surface = DarkSurface,
    surfaceVariant = Color.Black,
    onSurface = Color.White,
    error = RedWarning
)

private val DarkOledColorScheme = darkColorScheme(
    primary = Orange80,
    onPrimary = OrangeGrey80,
    background = Color.Black,
    surface = DarkSurface,
    surfaceVariant = DarkSurface,
    onSurface = Color.White,
    error = RedWarning
)

private val LightColorScheme = lightColorScheme(
    primary = Orange40,
    onPrimary = OrangeGrey40,
    surface = LightSurface,
    surfaceVariant = LightSurface,
    error = RedWarning

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun MoinoBudgetTheme(
    theme: Theme = Theme.Dark,
    content: @Composable () -> Unit
) = MaterialTheme(
    colorScheme = when (theme) {
        Theme.Dark -> DarkColorScheme
        Theme.Light -> LightColorScheme
        Theme.DarkOled -> DarkOledColorScheme
    },
    typography = Typography,
    content = content
)