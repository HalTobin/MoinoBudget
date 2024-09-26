package feature.expenses.feature.add_edit_expense.component

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import data.repository.AppPreferences
import kotlinx.datetime.LocalDate
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.close_dialog_description
import moinobudget.composeapp.generated.resources.date
import moinobudget.composeapp.generated.resources.select
import moinobudget.composeapp.generated.resources.select_day
import org.jetbrains.compose.resources.stringResource
import util.fullFormatDate
import util.toEpochMillisecond
import util.toLocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectDate(
    modifier: Modifier,
    preferences: AppPreferences,
    date: LocalDate,
    onDateSelect: (LocalDate) -> Unit
) {
    var selectDateDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = date.toEpochMillisecond())

    if (selectDateDialog) DatePickerDialog(
        shape = RoundedCornerShape(32.dp),
        colors = DatePickerDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background,
            selectedYearContainerColor = MaterialTheme.colorScheme.primary,
        ),
        onDismissRequest = { selectDateDialog = false },
        confirmButton = { Button(onClick = {
            datePickerState.selectedDateMillis?.let { onDateSelect(it.toLocalDate()) }
            selectDateDialog = false
        }) {
            Text(stringResource(Res.string.select))
        } }
    ) {
        Column {
            Box(Modifier.fillMaxWidth()) {
                Text(stringResource(Res.string.select_day).uppercase(),
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.Center))
                IconButton(
                    modifier = Modifier.align(Alignment.TopEnd).padding(8.dp),
                    onClick = { selectDateDialog = false }
                ) {
                    Icon(Icons.Default.Close, contentDescription = stringResource(Res.string.close_dialog_description))
                }
            }
            DatePicker(
                state = datePickerState,
                showModeToggle = false,
                title = null,
                headline = null,
                colors = DatePickerDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.background,
                    selectedYearContainerColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    }

    Column {
        Text(
            stringResource(Res.string.date),
            modifier = Modifier.padding(start = 16.dp),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(8.dp))
        Row(modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { selectDateDialog = true }
            .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Crossfade(modifier = Modifier.animateContentSize(),
                targetState = date) { currentDate ->
                Text(fullFormatDate(currentDate, preferences),
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
}