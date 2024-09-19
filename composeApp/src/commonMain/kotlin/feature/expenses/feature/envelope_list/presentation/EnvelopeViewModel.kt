package feature.expenses.feature.envelope_list.presentation

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

class EnvelopeViewModel(
    private val envelopeRepository: EnvelopeRepository
): ViewModel() {

    private val _state = MutableStateFlow(EnvelopeState())
    val state = _state.asStateFlow()

    private var envelopeJob: Job? = null

    init {
        setUpEnvelopeJob()
    }

    private fun setUpEnvelopeJob() {
        envelopeJob?.cancel()
        envelopeJob = viewModelScope.launch(Dispatchers.IO) {
            envelopeRepository.getEnvelopesFlow().collect { envelopes ->
                _state.update { it.copy(envelopes = envelopes) }
            }
        }
    }
}