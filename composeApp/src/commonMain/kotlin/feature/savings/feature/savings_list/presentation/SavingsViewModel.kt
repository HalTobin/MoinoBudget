package feature.savings.feature.savings_list.presentation

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

class SavingsViewModel(
    private val savingsRepository: SavingsRepository,
): ViewModel() {

    private val _state = MutableStateFlow(SavingsState())
    val state = _state.asStateFlow()

    private var savingsJob: Job? = null

    init {
        setUpSavingsJob()
    }

    private fun setUpSavingsJob() {
        savingsJob?.cancel()
        savingsJob = viewModelScope.launch(Dispatchers.IO) {
            savingsRepository.getSavingsFlow().collect { savings ->
                _state.update { it.copy(savings = savings) }
            }
        }
    }

}