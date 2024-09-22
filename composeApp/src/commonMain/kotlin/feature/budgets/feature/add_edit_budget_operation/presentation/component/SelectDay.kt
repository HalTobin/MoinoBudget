package feature.budgets.feature.add_edit_budget_operation.presentation.component

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.datetime.LocalDate
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.day
import moinobudget.composeapp.generated.resources.select_day
import org.jetbrains.compose.resources.stringResource
import presentation.data.MonthOption
import util.getLastDayOfMonth
import util.getMaxDay

@Composable
fun SelectDay(
    modifier: Modifier = Modifier,
    value: Int,
    onChange: (Int) -> Unit,
    date: LocalDate? = null,
    month: MonthOption?,
) {

    var dialogOpen by remember { mutableStateOf(false) }
    Column {
        Text(stringResource(Res.string.day),
            modifier = Modifier.padding(start = 16.dp),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(8.dp))
        Row(modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { dialogOpen = !dialogOpen }
            .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Crossfade(targetState = value) { selectedDay ->
                Text(selectedDay.toString(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 20.dp))
            }
            Icon(Icons.Default.CalendarMonth, contentDescription = null,
                modifier = Modifier.size(28.dp))
            Spacer(Modifier.width(4.dp))
        }
    }

    if (dialogOpen) Dialog(onDismissRequest = { dialogOpen = false } ) {
        Surface(
            color = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            shape = RoundedCornerShape(32.dp),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.surfaceVariant),
        ) {
            Column {
                Box(Modifier.padding(8.dp).fillMaxWidth()) {
                    Text(stringResource(Res.string.select_day),
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold)
                    IconButton(modifier = Modifier.align(Alignment.CenterEnd),
                        onClick = { dialogOpen = false} ) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                }
                LazyVerticalGrid(GridCells.Fixed(7),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    val max = date?.let {
                        getLastDayOfMonth(year = it.year, month = date.monthNumber)
                    } ?: month?.offset?.plus(1).getMaxDay()
                    val days = (1..max).map { it }
                    items(days) { day ->
                        val isSelected = (day == value)
                        Box(Modifier
                            .size(40.dp)
                            .then(
                                if (isSelected) Modifier.background(MaterialTheme.colorScheme.primary, CircleShape)
                                else Modifier)
                            .clip(CircleShape)
                            .clickable { onChange(day); dialogOpen = false },
                            contentAlignment = Alignment.Center) {
                            Text(day.toString(),
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

}