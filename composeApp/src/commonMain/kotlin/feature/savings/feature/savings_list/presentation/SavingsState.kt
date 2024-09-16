package feature.savings.feature.savings_list.presentation

import feature.savings.data.SavingsUI

data class SavingsState(
    val savings: List<SavingsUI> = emptyList(),
)