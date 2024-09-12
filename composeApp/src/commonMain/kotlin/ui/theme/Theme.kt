package ui.theme

import androidx.compose.material3.ColorScheme
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
    surfaceContainerHigh = Color.LightGray,
    onSurface = Color.White,
    error = RedWarning
)

private val DarkOledColorScheme = darkColorScheme(
    primary = Orange80,
    onPrimary = OrangeGrey80,
    background = Color.Black,
    surface = DarkSurface,
    surfaceVariant = DarkSurface,
    surfaceContainerHigh = Color.DarkGray,
    onSurface = Color.White,
    error = RedWarning
)

private val LightColorScheme = lightColorScheme(
    primary = Orange40,
    onPrimary = OrangeGrey40,
    surface = LightSurface,
    surfaceVariant = LightSurface,
    surfaceContainerHigh = Color.DarkGray,
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
    primary: Color? = null,
    onPrimary: Color? = null,
    content: @Composable () -> Unit
) = MaterialTheme(
    colorScheme = getColorScheme(theme, primary, onPrimary),
    typography = Typography,
    content = content
)

fun getColorScheme(theme: Theme, primary: Color?, onPrimary: Color?): ColorScheme {
    val colors = when (theme) {
        Theme.Dark -> DarkColorScheme
        Theme.Light -> LightColorScheme
        Theme.DarkOled -> DarkOledColorScheme
    }
    return if (primary != null && onPrimary != null) colors.copy(primary = primary, onPrimary = onPrimary)
    else colors
}