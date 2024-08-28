package feature.savings.presentation

import presentation.data.SavingsUI

data class SavingsState(
    val total: Float,
    val savings: List<SavingsUI>
)