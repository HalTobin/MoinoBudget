package presentation.data

import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.savings_type_account
import moinobudget.composeapp.generated.resources.savings_type_investments
import moinobudget.composeapp.generated.resources.savings_type_savings_books
import org.jetbrains.compose.resources.StringResource

enum class SavingsType(val id: Int, val text: StringResource) {
    Accounts(id = 0, text = Res.string.savings_type_account),
    SavingsBooks(id = 1, text = Res.string.savings_type_savings_books),
    Investments(id = 2, text = Res.string.savings_type_investments);

    companion object {
        fun findById(id: Int): SavingsType =
            SavingsType.entries.find { it.id == id } ?: Accounts
    }
}