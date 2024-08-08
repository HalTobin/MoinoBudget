package presentation.data

import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.annually
import moinobudget.composeapp.generated.resources.biannually
import moinobudget.composeapp.generated.resources.monthly
import moinobudget.composeapp.generated.resources.quarterly
import org.jetbrains.compose.resources.StringResource

enum class ExpenseFrequency(val id: Int, val multiplier: Int, val title: StringResource) {
    Monthly(id = 0, multiplier = 12, title = Res.string.monthly),
    Quarterly(id = 1, multiplier = 4, title = Res.string.quarterly),
    Biannually(id = 2, multiplier = 2, title = Res.string.biannually),
    Annually(id = 3, multiplier = 1, title = Res.string.annually)
}