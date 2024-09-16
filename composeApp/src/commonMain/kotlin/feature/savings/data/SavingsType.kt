package feature.savings.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.SsidChart
import androidx.compose.ui.graphics.vector.ImageVector
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.savings_type_accounts
import moinobudget.composeapp.generated.resources.savings_type_others
import moinobudget.composeapp.generated.resources.savings_type_investments
import moinobudget.composeapp.generated.resources.savings_type_savings_books
import org.jetbrains.compose.resources.PluralStringResource

enum class SavingsType(val id: Int, val tabId: Int, val text: PluralStringResource, val icon: ImageVector) {
    Accounts(id = 0, tabId = 0, text = Res.plurals.savings_type_accounts, icon = Icons.Default.AccountBalance),
    SavingsBooks(id = 1, tabId = 1, text = Res.plurals.savings_type_savings_books, icon = Icons.Default.Book),
    Investments(id = 3, tabId = 2, text = Res.plurals.savings_type_investments, icon = Icons.Default.SsidChart),
    Other(id = 2, tabId = 3, text = Res.plurals.savings_type_others, icon = Icons.Default.Mail);

    companion object {
        fun findById(id: Int): SavingsType =
            SavingsType.entries.find { it.id == id } ?: Accounts
        fun findByTabId(tabId: Int): SavingsType =
            SavingsType.entries.find { it.tabId == tabId } ?: Accounts
    }
}