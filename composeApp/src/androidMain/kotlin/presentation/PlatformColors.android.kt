package presentation

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun PlatformColors(statusBarColor: Color, navBarColor: Color){
    val activity = LocalContext.current as ComponentActivity
    val window = activity.window

    SideEffect {
        window.statusBarColor = statusBarColor.toArgb()
        window.navigationBarColor = navBarColor.toArgb()
    }
}