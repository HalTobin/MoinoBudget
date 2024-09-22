package feature.expenses.feature.add_edit_envelope.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.repository.EnvelopeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import util.nullIfMinus

class AddEditEnvelopeViewModel(
    envelopeId: Int,
    private val envelopeRepository: EnvelopeRepository
): ViewModel() {

    private val _state = MutableStateFlow(AddEditEnvelopeState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        envelopeId.nullIfMinus()?.let { loadEnvelope(it) }
    }

    private fun loadEnvelope(envelopeId: Int) = viewModelScope.launch(Dispatchers.IO) {
        envelopeRepository.getEnvelopeById(envelopeId)?.let { envelope ->
            _state.update { AddEditEnvelopeState.generateFromEnvelopeUI(envelope) }
        }
    }

    fun onEvent(event: AddEditEnvelopeEvent) {
        when (event) {
            is AddEditEnvelopeEvent.UpsertEnvelope -> viewModelScope.launch(Dispatchers.IO) {
                _state.value.getAddEditEnvelope()?.let { addEditEnvelope ->
                    envelopeRepository.upsertEnvelope(addEditEnvelope)
                }
            }
            is AddEditEnvelopeEvent.DeleteEnvelope -> viewModelScope.launch(Dispatchers.IO) {
                _state.value.envelopeId?.let { envelopeId ->
                    envelopeRepository.deleteEnvelope(envelopeId)
                    _eventFlow.emit(UiEvent.DeleteQuit)
                }
            }
            is AddEditEnvelopeEvent.UpdateTitle -> _state.update { it.copy(title = event.title) }
            is AddEditEnvelopeEvent.UpdateFrequency -> _state.update { it.copy(frequency = event.frequencyId) }
            is AddEditEnvelopeEvent.UpdateMax -> _state.update { it.copy(max = event.max) }
            is AddEditEnvelopeEvent.UpdateIcon -> _state.update { it.copy(iconId = event.iconId) }
            is AddEditEnvelopeEvent.UpdateColor -> _state.update { it.copy(color = event.color) }
        }
    }

    sealed class UiEvent {
        data object DeleteQuit: UiEvent()
    }

}