package feature.savings.feature.savings_detail

sealed class SavingsDetailsEvent {
    data class UpdateAmount(val amount: Int, val operation: Operation): SavingsDetailsEvent()
    data class UpdateAmountField(val amount: String): SavingsDetailsEvent()
}

enum class Operation { Add, Subtract }