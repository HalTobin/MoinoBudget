package feature.savings.presentation

import presentation.data.LabelUI
import presentation.data.SavingsUI

data class SavingsState(
    val total: Float = 0f,
    val savings: List<SavingsUI> = emptyList(),
    val labels: List<LabelUI> = emptyList()
)