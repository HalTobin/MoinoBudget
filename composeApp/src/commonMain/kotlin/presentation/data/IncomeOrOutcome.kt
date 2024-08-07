package presentation.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.incomes
import moinobudget.composeapp.generated.resources.outcomes
import org.jetbrains.compose.resources.StringResource

enum class IncomeOrOutcome(
    val dbId: Boolean,
    val tabId: Int,
    val text: StringResource,
    val color: Color,
    val icon: ImageVector
) {
    Income(dbId = true, tabId = 1, text = Res.string.incomes, color = Color.Green, icon = Icons.Default.AccountBalanceWallet),
    Outcome(dbId = false, tabId = 0, text = Res.string.outcomes, color = Color.Red, icon = Icons.Default.CreditCard)
}