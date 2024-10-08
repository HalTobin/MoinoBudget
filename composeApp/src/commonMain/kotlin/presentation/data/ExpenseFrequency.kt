package presentation.data

import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.annually
import moinobudget.composeapp.generated.resources.biannually
import moinobudget.composeapp.generated.resources.monthly
import moinobudget.composeapp.generated.resources.quarterly
import org.jetbrains.compose.resources.StringResource

enum class ExpenseFrequency(val id: Int, val everyXMonth: Int, val multiplier: Int, val title: StringResource, val options: List<MonthOption>) {
    Monthly(id = 0, everyXMonth = 1, multiplier = 12, title = Res.string.monthly, options = emptyList()),
    Annually(id = 1, everyXMonth = 12, multiplier = 1, title = Res.string.annually, options = MonthOption.annualOptions),

    Quarterly(id = 2, everyXMonth = 3, multiplier = 4, title = Res.string.quarterly, options = MonthOption.quarterlyOptions),
    Biannually(id = 3, everyXMonth = 6, multiplier = 2, title = Res.string.biannually, options = MonthOption.biannualOptions);

    companion object {
        val list = listOf(Monthly, /*Quarterly, Biannually,*/ Annually)

        fun findById(id: Int): ExpenseFrequency = list.find { it.id == id } ?: Monthly
    }
}