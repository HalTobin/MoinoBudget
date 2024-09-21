package feature.savings.feature.add_edit_savings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.repository.SavingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import util.nullIfMinus

class AddEditSavingsViewModel(
    savingsId: Int,
    private val savingsRepository: SavingsRepository
): ViewModel() {

    private val _state = MutableStateFlow(AddEditSavingsState())
    val state = _state.asStateFlow()

    init {
        savingsId.nullIfMinus()?.let { loadSavings(it) }
    }

    fun onEvent(event: AddEditSavingsEvent) {
        when (event) {
            is AddEditSavingsEvent.UpsertSavings -> _state.value.generateAddEditSavings()?.let { addEditSavings ->
                viewModelScope.launch { savingsRepository.upsertSavings(addEditSavings) }
            }
            is AddEditSavingsEvent.DeleteSavings -> _state.value.savingsId?.let { savingsId ->
                viewModelScope.launch { savingsRepository.deleteSavings(savingsId) }
            }

            is AddEditSavingsEvent.UpdateTitle -> _state.update { it.copy(savingsTitle = event.title) }
            is AddEditSavingsEvent.UpdateSubtitle -> _state.update { it.copy(savingsSubtitle = event.subtitle) }
            is AddEditSavingsEvent.UpdateAmount -> _state.update { it.copy(savingsAmount = event.amount) }
            is AddEditSavingsEvent.UpdateGoal -> _state.update { it.copy(savingsGoal = event.goal) }
            is AddEditSavingsEvent.UpdateType -> _state.update { it.copy(savingsType = event.type) }
            is AddEditSavingsEvent.UpdateColor -> _state.update { it.copy(savingsColor = event.color) }
            is AddEditSavingsEvent.UpdateIcon -> _state.update {
                it.copy(savingsIconId =
                    if (event.iconId == _state.value.savingsIconId) null
                    else event.iconId) }
        }
    }

    private fun loadSavings(savingsId: Int) = viewModelScope.launch(Dispatchers.IO) {
        val savings = savingsRepository.getSavingsById(savingsId)
        _state.update { AddEditSavingsState.generateStateFromSavingsUI(savings) }
    }

}