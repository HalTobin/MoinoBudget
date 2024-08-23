package presentation.data

import androidx.compose.runtime.Composable
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.april
import moinobudget.composeapp.generated.resources.august
import moinobudget.composeapp.generated.resources.december
import moinobudget.composeapp.generated.resources.february
import moinobudget.composeapp.generated.resources.january
import moinobudget.composeapp.generated.resources.july
import moinobudget.composeapp.generated.resources.june
import moinobudget.composeapp.generated.resources.march
import moinobudget.composeapp.generated.resources.may
import moinobudget.composeapp.generated.resources.november
import moinobudget.composeapp.generated.resources.october
import moinobudget.composeapp.generated.resources.september
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

enum class MonthOption(val textResources: List<StringResource>, val offset: Int) {
    January(textResources = listOf(Res.string.january), offset = 0),
    February(textResources = listOf(Res.string.february), offset = 1),
    March(textResources = listOf(Res.string.march), offset = 2),
    April(textResources = listOf(Res.string.april), offset = 3),
    May(textResources = listOf(Res.string.may), offset = 4),
    June(textResources = listOf(Res.string.june), offset = 5),
    July(textResources = listOf(Res.string.july), offset = 6),
    August(textResources = listOf(Res.string.august), offset = 7),
    September(textResources = listOf(Res.string.september), offset = 8),
    October(textResources = listOf(Res.string.october), offset = 9),
    November(textResources = listOf(Res.string.november), offset = 10),
    December(textResources = listOf(Res.string.december), offset = 11),

    JaJu(textResources = listOf(Res.string.january, Res.string.july), offset = 0),
    FeAu(textResources = listOf(Res.string.february, Res.string.august), offset = 1),
    MaSe(textResources = listOf(Res.string.march, Res.string.september), offset = 2),
    ApOc(textResources = listOf(Res.string.april, Res.string.october), offset = 3),
    MaNo(textResources = listOf(Res.string.may, Res.string.november), offset = 4),
    JuDe(textResources = listOf(Res.string.june, Res.string.december), offset = 5),

    JaApJuOc(textResources = listOf(Res.string.january, Res.string.april, Res.string.july, Res.string.october), offset = 0),
    FeMaAuNo(textResources = listOf(Res.string.february, Res.string.may, Res.string.august, Res.string.november), offset = 1),
    MaJuSeDe(textResources = listOf(Res.string.march, Res.string.june, Res.string.september, Res.string.december), offset = 2);

    @Composable
    fun getText(): String {
        val text = StringBuilder()
        textResources.forEachIndexed { index, stringResource ->
            text.append(stringResource(stringResource))
            if (index != textResources.lastIndex) text.append(", ")
        }
        return text.toString()
    }

    companion object {
        val annualOptions = listOf(January, February, March, April, May, June, July,
            August, September, October, November, December)
        val biannualOptions = listOf(JaJu, FeAu, MaSe, ApOc, MaNo, JuDe)
        val quarterlyOptions = listOf(JaApJuOc, FeMaAuNo, MaJuSeDe)

        fun findByOffsetAndFrequency(frequency: ExpenseFrequency, offset: Int?): MonthOption? {
            return if (offset == null || frequency == ExpenseFrequency.Monthly) null
            else when (frequency) {
                ExpenseFrequency.Monthly -> emptyList()
                ExpenseFrequency.Annually -> annualOptions
                ExpenseFrequency.Quarterly -> quarterlyOptions
                ExpenseFrequency.Biannually -> biannualOptions
            }.find { it.offset == offset }
        }
    }
}