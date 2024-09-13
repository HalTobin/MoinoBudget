package presentation.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.SsidChart
import androidx.compose.ui.graphics.vector.ImageVector
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.savings_type_accounts
import moinobudget.composeapp.generated.resources.savings_type_envelopes
import moinobudget.composeapp.generated.resources.savings_type_investments
import moinobudget.composeapp.generated.resources.savings_type_savings_books
import org.jetbrains.compose.resources.PluralStringResource

enum class SavingsType(val id: Int, val text: PluralStringResource, val icon: ImageVector) {
    Accounts(id = 0, text = Res.plurals.savings_type_accounts, icon = Icons.Default.AccountBalance),
    SavingsBooks(id = 1, text = Res.plurals.savings_type_savings_books, icon = Icons.Default.Book),
    Envelope(id = 2, text = Res.plurals.savings_type_envelopes, icon = Icons.Default.Mail),
    Investments(id = 3, text = Res.plurals.savings_type_investments, icon = Icons.Default.SsidChart);

    companion object {
        fun findById(id: Int): SavingsType =
            SavingsType.entries.find { it.id == id } ?: Accounts
    }
}