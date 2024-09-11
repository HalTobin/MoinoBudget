package feature.savings.feature.add_edit_savings.presentation

import androidx.compose.ui.graphics.Color
import feature.savings.feature.add_edit_savings.data.AddEditSavings
import presentation.data.SavingsType
import presentation.data.SavingsUI

data class AddEditSavingsState(
    val savingsId: Int? = null,
    val savingsTitle: String = "",
    val savingsSubtitle: String = "",
    val savingsIconId: Int? = null,
    val savingsType: SavingsType = SavingsType.Accounts,
    val savingsAmount: String = "",
    val savingsGoal: String = "",
    //val savingsAutoIncrement: Int,
    //val savingsLastAutoIncrement: LocalDate?,
    val savingsColor: Color? = null
) {

    fun generateAddEditSavings(): AddEditSavings? =
        savingsAmount.toIntOrNull()?.let { amount ->
            AddEditSavings(
                id = savingsId,
                title = savingsTitle,
                subtitle = savingsSubtitle,
                iconId = savingsIconId,
                type = savingsType,
                amount = amount,
                goal = savingsGoal.toIntOrNull(),
                autoIncrement = 0,
                lastMonthAutoIncrement = null,
                color = savingsColor
            )
        }

    companion object {
        fun generateStateFromSavingsUI(savings: SavingsUI): AddEditSavingsState =
            AddEditSavingsState(
                savingsId = savings.id,
                savingsTitle = savings.title,
                savingsSubtitle = savings.subtitle,
                savingsIconId = savings.icon?.id,
                savingsType = savings.type,
                savingsAmount = savings.amount.toString(),
                savingsGoal = savings.goal?.toString() ?: "",
                savingsColor = savings.color,
            )
    }

}