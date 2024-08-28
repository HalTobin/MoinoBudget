package feature.dashboard.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Filter1
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.ui.graphics.vector.ImageVector
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.sort_amount
import moinobudget.composeapp.generated.resources.sort_category
import moinobudget.composeapp.generated.resources.sort_date
import moinobudget.composeapp.generated.resources.sort_frequency
import org.jetbrains.compose.resources.StringResource
import presentation.data.ExpenseUI

enum class ExpenseSort(val text: StringResource, val icon: ImageVector) {
    Date(text = Res.string.sort_date, icon = Icons.Default.CalendarMonth),
    Amount(text = Res.string.sort_amount, icon = Icons.Default.Filter1),
    Frequency(text = Res.string.sort_frequency, icon = Icons.Default.Timelapse),
    Category(text = Res.string.sort_category, icon = Icons.Default.Category);

    companion object {
        val list = listOf(Date, Amount, Frequency, Category)
    }
}

fun List<ExpenseUI>.expenseSort(sortingMethod: ExpenseSort): List<ExpenseUI> =
    when (sortingMethod) {
        ExpenseSort.Date -> this.sortedBy { it.dueIn }
        ExpenseSort.Amount -> this.sortedByDescending { it.amount }
        ExpenseSort.Frequency -> this.sortedBy { it.frequency }
        ExpenseSort.Category -> this.sortedBy { it.icon }
    }