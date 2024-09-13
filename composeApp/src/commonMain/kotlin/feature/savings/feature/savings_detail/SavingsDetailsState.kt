package feature.savings.feature.savings_detail

import presentation.data.SavingsUI

data class SavingsDetailsState(
    val savings: SavingsUI? = null,
    val amountText: String = ""
)