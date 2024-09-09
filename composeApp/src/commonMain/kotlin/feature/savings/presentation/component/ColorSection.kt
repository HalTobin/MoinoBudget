package feature.savings.presentation.component

import androidx.compose.animation.Crossfade
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColorSection(
    selection: Color?,
    colorSelect: (Color?) -> Unit
) = BoxWithConstraints {
    val offSet = maxWidth / 5

    val colors = DefaultColors.entries
    val colorPager = rememberPagerState(initialPage = colors.size / 2, pageCount = { colors.size })
    HorizontalPager(colorPager,
        Modifier.fillMaxWidth()
            .padding(vertical = 8.dp),
        contentPadding = PaddingValues(horizontal = (maxWidth - 50.dp) / 2),
        pageSpacing = offSet - 50.dp,
    ) { page ->
        IconButton(onClick = {
            //selection = if (selection == page) null else page
            colorSelect(colors.getOrNull(page)?.color)
        }) {
            Crossfade(targetState = colors[page].color == selection) { selected ->
                Box(Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(colors[page].color, CircleShape)
                    .then(
                        if (selected) Modifier.border(4.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        else Modifier.border(2.dp, Color.Black, CircleShape)
                    )
                )
            }
        }
    }
}

enum class DefaultColors(val color: Color) {
    White(Color(0xFFFFFFFF)),
    Black(Color(0xFF000000)),

    Cyan(Color(0xFF0FBAD5)),
    SkyBlue(Color(0xFF0052FF)),

    Purple(Color(0xFFD10073)),
    Violet(Color(0xFF9E68B5)),

    LightGreen(Color(0xFF84fb7f)),
    ForestGreen(Color(0xFF5FA025)),

}