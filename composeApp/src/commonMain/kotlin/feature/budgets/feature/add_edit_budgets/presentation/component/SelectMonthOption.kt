package feature.budgets.feature.add_edit_budgets.presentation.component

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.month
import org.jetbrains.compose.resources.stringResource
import presentation.data.MonthOption

@Composable
fun SelectMonthOption(
    modifier: Modifier = Modifier,
    options: List<MonthOption>,
    value: MonthOption?,
    onSelect: (MonthOption) -> Unit
) {
    var expandedMenu by remember { mutableStateOf(false) }

    val rotationAngle by animateFloatAsState(
        targetValue = if (expandedMenu) 180f else 0f,
        animationSpec = tween(300),
        label = "rotation for the dropdown menu's icon"
    )

    Column {
        Text(
            stringResource(Res.string.month),
            modifier = Modifier.padding(start = 16.dp),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(8.dp))
        Row(modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { expandedMenu = !expandedMenu }
            .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Crossfade(
                targetState = value,
                modifier = Modifier.weight(1f)
            ) { selectedMonth ->
                Text(
                    selectedMonth?.getText() ?: "",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth().padding(start = 12.dp)
                )
            }
            Icon(
                Icons.Default.ArrowDropDown,
                modifier = Modifier.size(28.dp).rotate(rotationAngle),
                contentDescription = null
            )
        }
    }

    DropdownMenu(
        shape = RoundedCornerShape(16.dp),
        expanded = expandedMenu,
        onDismissRequest = { expandedMenu = false }
    ) {
        options.forEach { option ->
            DropdownMenuItem(
                onClick = { onSelect(option); expandedMenu = false },
                text = {
                    Text(option.getText(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 8.dp)) }
            )
        }
    }
}