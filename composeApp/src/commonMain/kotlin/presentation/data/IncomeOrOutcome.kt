package presentation.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.income
import moinobudget.composeapp.generated.resources.incomes
import moinobudget.composeapp.generated.resources.outcome
import moinobudget.composeapp.generated.resources.outcomes
import org.jetbrains.compose.resources.StringResource

enum class IncomeOrOutcome(
    val id: Int,
    val dbId: Boolean,
    val tabId: Int,
    val text: StringResource,
    val textSingular: StringResource,
    val color: Color,
    val icon: ImageVector
) {
    Outcome(id = 0, dbId = false, tabId = 1, text = Res.string.outcomes, textSingular = Res.string.outcome, color = Color(0xFFF70A0A), icon = Icons.Default.CreditCard),
    Income(id = 1, dbId = true, tabId = 0, text = Res.string.incomes, textSingular = Res.string.income, color = Color(0xFF1FC600), icon = Icons.Default.AccountBalanceWallet);

    companion object {
        val list = listOf(Income, Outcome)
        fun getByDbId(dbId: Boolean): IncomeOrOutcome =
            if (dbId == Income.dbId) Income else Outcome
        fun getByTabId(tabId: Int): IncomeOrOutcome =
            if (tabId == Income.tabId) Income else Outcome
        fun getById(id: Int): IncomeOrOutcome =
            if (id == Income.id) Income else Outcome
    }
}