package feature.dashboard.presentation.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import feature.dashboard.presentation.dialog.LabelEntry
import presentation.data.LabelUI
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