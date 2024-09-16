package feature.budgets.feature.budgets_list.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import feature.budgets.data.LabelUI
import presentation.shake

@Composable
fun LabelSelection(
    labels: List<LabelUI>,
    selected: List<Int>,
    onSelect: (Int) -> Unit,
    deleteMode: Boolean = false
) = LazyVerticalGrid(
    GridCells.Fixed(4),
    modifier = Modifier.padding(horizontal = 16.dp),
    contentPadding = PaddingValues(4.dp)
) {
    items(labels) { label ->
        LabelEntry(label = label,
            modifier = Modifier.animateItem().shake(deleteMode),
            selectedLabels = selected,
            onClick = { onSelect(label.id) },
        )
    }
}

@Composable
fun LabelEntry(
    modifier: Modifier,
    label: LabelUI,
    selectedLabels: List<Int>,
    onClick: () -> Unit
) {
    val selected = selectedLabels.any { it == label.id }
    Box(modifier = modifier
        .aspectRatio(1f)
        .padding(horizontal = 4.dp, vertical = 6.dp)
        .clip(RoundedCornerShape(8.dp))
        .background(
            if (selected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.surface)
        .clickable { onClick() }
        .padding(4.dp),
        contentAlignment = Alignment.Center) {
        Box(Modifier
            .size(30.dp)
            .clip(CircleShape)
            .border(2.dp, Color.Black, CircleShape)
            .background(label.color)
        )
    }
}