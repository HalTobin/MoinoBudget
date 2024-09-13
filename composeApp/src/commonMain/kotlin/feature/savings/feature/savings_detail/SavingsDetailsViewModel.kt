package feature.savings.feature.savings_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.repository.SavingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SavingsDetailsViewModel(
    private val savingsRepository: SavingsRepository
): ViewModel() {
    private val _state = MutableStateFlow(SavingsDetailsState())
    val state = _state.asStateFlow()

    private var savingsJob: Job? = null

    fun onEvent(event: SavingsDetailsEvent) {
        when (event) {
            is SavingsDetailsEvent.Init -> setSavingsJob(event.savingsId)
            is SavingsDetailsEvent.UpdateAmount -> viewModelScope.launch(Dispatchers.IO) {
                _state.value.savings?.let { savings ->
                    val newAmount = when (event.operation) {
                        Operation.Add -> savings.amount + event.amount
                        Operation.Subtract -> savings.amount - event.amount
                    }
                    savingsRepository.updateAmount(id = savings.id, newAmount = newAmount)
                    _state.update { it.copy(amountText = "") }
                }
            }
            is SavingsDetailsEvent.UpdateAmountField -> _state.update {
                it.copy(amountText = event.amount) }
        }
    }

    private fun setSavingsJob(savingsId: Int) {
        savingsJob?.cancel()
        savingsJob = viewModelScope.launch(Dispatchers.IO) {
            savingsRepository.getSavingsFlowById(savingsId).collect { savings ->
                _state.update { it.copy(savings = savings) }
            }
        }
    }

}