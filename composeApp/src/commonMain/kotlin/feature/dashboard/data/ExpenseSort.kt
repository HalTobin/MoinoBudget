package feature.dashboard.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Filter1
import androidx.compose.ui.graphics.vector.ImageVector
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.sort_amount
import moinobudget.composeapp.generated.resources.sort_category
import moinobudget.composeapp.generated.resources.sort_date
import org.jetbrains.compose.resources.StringResource
import presentation.data.ExpenseUI
import presentation.data.IncomeOrOutcome

enum class ExpenseSort(val text: StringResource, val icon: ImageVector) {
    Date(text = Res.string.sort_date, icon = Icons.Default.CalendarMonth),
    Amount(text = Res.string.sort_amount, icon = Icons.Default.Filter1),
    //Type(text = Res.string.sort_amount, icon = Icons.Default.Plus),
    //Frequency(text = Res.string.sort_frequency, icon = Icons.Default.Timelapse),
    Category(text = Res.string.sort_category, icon = Icons.Default.Category);

    companion object {
        val list = ExpenseSort.entries
    }
}

fun List<ExpenseUI>.expenseSort(typeFilter: IncomeOrOutcome?,sortingMethod: ExpenseSort): List<ExpenseUI> =
    when (sortingMethod) {
        ExpenseSort.Date -> this.sortedBy { it.dueIn }
        ExpenseSort.Amount -> this.sortedByDescending { it.amount }
        //ExpenseSort.Frequency -> this.sortedBy { it.frequency }
        ExpenseSort.Category -> this.sortedBy { it.icon }
    }.filter { typeFilter?.let { type ->
        it.type == type
    } ?: true }