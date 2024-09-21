package feature.expenses.feature.envelope_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.repository.EnvelopeRepository
import data.repository.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import util.nullIfMinus

class EnvelopeDetailsViewModel(
    envelopeId: Int,
    private val envelopeRepository: EnvelopeRepository,
    private val expenseRepository: ExpenseRepository
): ViewModel() {

    private val _state = MutableStateFlow(EnvelopeDetailsState())
    val state = _state.asStateFlow()

    private var envelopeJob: Job? = null
    private var expensesJob: Job? = null

    init {
        envelopeId.nullIfMinus()?.let {
            listenToEnvelope(it)
            listenToExpenses(it)
        }
    }

    private fun listenToEnvelope(envelopeId: Int) {
        envelopeJob?.cancel()
        envelopeJob = viewModelScope.launch(Dispatchers.IO) {
            envelopeRepository.getEnvelopeFlowById(envelopeId).collect { envelope ->
                _state.update { it.copy(envelope = envelope) }
            }
        }
    }

    private fun listenToExpenses(envelopeId: Int) {
        expensesJob?.cancel()
        expensesJob = viewModelScope.launch(Dispatchers.IO) {
            expenseRepository.getExpensesFlowByEnvelopeId(envelopeId).collect { expenses ->
                _state.update { it.copy(expenses = expenses) }
            }
        }
    }

}