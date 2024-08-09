package presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import presentation.data.Background

@Composable
fun LabelBackground(modifier: Modifier = Modifier,
                    background: Background) = when (background) {
    is Background.Gradient -> Box(modifier = modifier.background(Brush.linearGradient(background.colors)))
    is Background.Image -> Image(
        modifier = modifier,
        painter = painterResource(background.image),
        contentScale = ContentScale.Crop,
        contentDescription = null
    )
}