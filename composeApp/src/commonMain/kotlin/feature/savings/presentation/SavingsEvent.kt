package feature.savings.presentation

import feature.savings.data.AddEditSavings

sealed class SavingsEvent {
    data class UpsertSavings(val savings: AddEditSavings): SavingsEvent()
    data class DeleteSavings(val savingsId: Int): SavingsEvent()
}