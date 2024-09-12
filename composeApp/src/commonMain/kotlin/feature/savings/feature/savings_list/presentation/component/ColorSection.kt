package feature.savings.feature.savings_list.presentation.component

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ui.theme.Cyan80
import ui.theme.Green80
import ui.theme.Orange80
import ui.theme.Red80

@Composable
fun ColorSection(
    modifier: Modifier,
    selection: Color?,
    colorSelect: (Color?) -> Unit
) = BoxWithConstraints {
    val offSet = maxWidth / 6

    val colors = DefaultColors.colors
    val colorPager = rememberPagerState(initialPage = colors.size / 2, pageCount = { colors.size })

    LaunchedEffect(key1 = colorSelect) {
        selection?.let { color ->
            val current = colors.find { it == color }
            val currentIndex = colors.indexOf(current)
            colorPager.animateScrollToPage(currentIndex, animationSpec = tween(500))
        }
    }

    HorizontalPager(colorPager,
        modifier.fillMaxWidth()
            .padding(vertical = 8.dp),
        contentPadding = PaddingValues(horizontal = (maxWidth - 50.dp) / 2),
        pageSpacing = offSet - 50.dp,
    ) { page ->
        IconButton(onClick = {
            //selection = if (selection == page) null else page
            colorSelect(colors.getOrNull(page))
        }) {
            Crossfade(targetState = colors[page] == selection) { selected ->
                Box(Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(colors[page], CircleShape)
                    .then(
                        if (selected) Modifier.border(4.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        else Modifier.border(2.dp, Color.Black, CircleShape)
                    )
                )
            }
        }
    }
}

object DefaultColors {

    val colors = listOf(
        Color(0xFFFFFFFF), // White
        Color(0xFF000000), // Black

        Color(0xFF5FA025), // ForestGreen
        Green80,
        Color(0xFF84fb7f), // LightGreen

        Color(0xFF0FBAD5), // Cyan
        Cyan80,
        Color(0xFF0052FF), // SkyBlue

        Color(0xFF9E68B5), // Violet
        Color(0xFFD10073), // Purple
        Red80,
        Orange80,
    )
}