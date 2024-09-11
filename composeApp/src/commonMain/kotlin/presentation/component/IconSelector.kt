package presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import feature.expenses.add_edit_expense.presentation.AddEditExpenseEvent
import presentation.data.ExpenseIcon
import presentation.shake

@Composable
fun IconSelector(
    modifier: Modifier,
    selectedIcon: Int?,
    onSelect: (Int) -> Unit,
    deleteMode: Boolean
) = LazyHorizontalGrid(
    GridCells.Fixed(2),
    contentPadding = PaddingValues(horizontal = 24.dp),
    modifier = modifier.height(128.dp)) {
    items(ExpenseIcon.list) { icon ->
        IconEntry(icon = icon,
            selectedIcon = selectedIcon,
            onClick = { onSelect(icon.id) },
            deleteMode = deleteMode
        )
    }
}

@Composable
fun IconEntry(
    icon: ExpenseIcon,
    selectedIcon: Int?,
    deleteMode: Boolean,
    onClick: () -> Unit,
) {
    val selected = selectedIcon == icon.id
    Box(modifier = Modifier
        .shake(deleteMode)
        .aspectRatio(1f)
        .padding(horizontal = 4.dp, vertical = 6.dp)
        .clip(RoundedCornerShape(8.dp))
        .background(
            if (selected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.surface)
        .clickable { onClick() }
        .padding(4.dp),
        contentAlignment = Alignment.Center) {
        Icon(modifier = Modifier.size(30.dp),
            tint = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground,
            imageVector = icon.icon,
            contentDescription = null)
    }
}