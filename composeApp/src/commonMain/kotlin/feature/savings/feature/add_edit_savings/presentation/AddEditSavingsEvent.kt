package feature.savings.feature.add_edit_savings.presentation

import androidx.compose.ui.graphics.Color
import feature.savings.data.SavingsType

sealed class AddEditSavingsEvent {
    data class UpdateTitle(val title: String): AddEditSavingsEvent()
    data class UpdateSubtitle(val subtitle: String): AddEditSavingsEvent()
    data class UpdateType(val type: SavingsType): AddEditSavingsEvent()
    data class UpdateAmount(val amount: String): AddEditSavingsEvent()
    data class UpdateGoal(val goal: String): AddEditSavingsEvent()
    data class UpdateColor(val color: Color?): AddEditSavingsEvent()
    data class UpdateIcon(val iconId: Int?): AddEditSavingsEvent()
    data object UpsertSavings: AddEditSavingsEvent()
    data object DeleteSavings: AddEditSavingsEvent()
}