package feature.expenses.feature.add_edit_envelope.presentation

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

class AddEditEnvelopeViewModel(
    private val envelopeRepository: EnvelopeRepository
): ViewModel() {

    private val _state = MutableStateFlow(AddEditEnvelopeState())
    val state = _state.asStateFlow()

    private fun loadEnvelope(envelopeId: Int) = viewModelScope.launch(Dispatchers.IO) {
        if (envelopeId != -1) {
            val envelope = envelopeRepository.getEnvelopeById(envelopeId)
            _state.update { AddEditEnvelopeState.generateFromEnvelopeUI(envelope) }
        }
    }

    fun onEvent(event: AddEditEnvelopeEvent) {
        when (event) {
            is AddEditEnvelopeEvent.Init -> loadEnvelope(event.envelopeId)
            is AddEditEnvelopeEvent.UpsertEnvelope -> viewModelScope.launch(Dispatchers.IO) {
                _state.value.getAddEditEnvelope()?.let { addEditEnvelope ->
                    envelopeRepository.upsertEnvelope(addEditEnvelope)
                }
            }
            is AddEditEnvelopeEvent.DeleteEnvelope -> viewModelScope.launch(Dispatchers.IO) {
                _state.value.envelopeId?.let { envelopeId ->
                    envelopeRepository.deleteEnvelope(envelopeId)
                }
            }
            is AddEditEnvelopeEvent.UpdateTitle -> _state.update { it.copy(title = event.title) }
            is AddEditEnvelopeEvent.UpdateFrequency -> _state.update { it.copy(frequency = event.frequencyId) }
            is AddEditEnvelopeEvent.UpdateMax -> _state.update { it.copy(max = event.max) }
            is AddEditEnvelopeEvent.UpdateIcon -> _state.update { it.copy(iconId = event.iconId) }
        }
    }

}