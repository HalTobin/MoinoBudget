package feature.expenses.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun CustomProgressBar(
    modifier: Modifier,
    height: Dp = 20.dp,
    textSize: TextUnit = 16.sp,
    current: Double,
    max: Int,
    primaryColor: Color?,
    onPrimaryColor: Color?
) {
    val barPrimaryColor = primaryColor ?: MaterialTheme.colorScheme.primary
    val barOnPrimaryColor = onPrimaryColor ?: MaterialTheme.colorScheme.onPrimary

    val currentProgress = remember { Animatable(0f) }
    val progress = current.toFloat() / max

    LaunchedEffect(key1 = progress) {
        delay(300)
        currentProgress.animateTo(
            targetValue = progress,
            animationSpec = tween(1500)
        )
    }

    Box(
        modifier
            .clip(CircleShape)
            .height(height)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .fillMaxWidth()) {
        Box(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(currentProgress.value)
                .clip(CircleShape)
                .background(barPrimaryColor)) {
            Text("${(progress * 100).toInt()}%",
                modifier = Modifier.align(Alignment.Center),
                fontWeight = FontWeight.SemiBold,
                color = barOnPrimaryColor,
                fontSize = textSize,
                style = MaterialTheme.typography.titleSmall)
        }
    }
}