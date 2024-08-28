package feature.savings.presentation

import androidx.lifecycle.ViewModel
import data.repository.SavingsRepository

class SavingsViewModel(
    private val savingsRepository: SavingsRepository
): ViewModel() {
}