package feature.expenses.feature.envelope_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.repository.EnvelopeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EnvelopeHistoryViewModel(
    envelopeId: Int,
    private val envelopeRepository: EnvelopeRepository
): ViewModel() {

    private var envelopeJob: Job? = null

    private val _state = MutableStateFlow(EnvelopeHistoryState())
    val state = _state.asStateFlow()

    init {
        listenEnvelopeHistory(envelopeId)
    }

    private fun listenEnvelopeHistory(envelopeId: Int) {
        envelopeJob?.cancel()
        envelopeJob = viewModelScope.launch(Dispatchers.IO) {
            envelopeRepository.getEnvelopeHistoryFlowByEnvelopeId(envelopeId).collect { history ->
                _state.update { it.copy(envelopes = history.sortedByDescending { it.startPeriod }) }
            }
        }
    }

}