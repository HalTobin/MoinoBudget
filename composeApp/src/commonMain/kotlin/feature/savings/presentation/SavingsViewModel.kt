package feature.savings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.db.table.Savings
import data.mapper.toSavingsUI
import data.repository.LabelRepository
import data.repository.SavingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import presentation.data.LabelUI
import presentation.data.SavingsUI

class SavingsViewModel(
    private val savingsRepository: SavingsRepository,
    private val labelRepository: LabelRepository
): ViewModel() {

    private val _state = MutableStateFlow(SavingsState())
    val state = _state.asStateFlow()

    private var savingsJob: Job? = null
    private var labels: List<LabelUI> = emptyList()

    private val dummySavings = listOf(
        Savings(1, "Livret A", "Fortuneo", 575, 22950, 0, 0, labelId = 0),
        Savings(2, "LEP", "Hello Bank", 750, 10000, 0, 0, labelId = 2),
        Savings(3, "LDD", "Crédit Agricole", 2000, 12000, 0, 0, labelId = 3)
    )

    init {
        setUpSavingsJob()
        viewModelScope.launch(Dispatchers.IO) {
            labels = labelRepository.getLabels()
            _state.update { it.copy(
                //total = dummySavings.sumOf { it.amount }.toFloat(),
                labels = labels,
                //savings = dummySavings.map { it.toSavingsUI(labels) }
            ) }
        }
    }

    fun onEvent(event: SavingsEvent) = viewModelScope.launch {
        when (event) {
            is SavingsEvent.UpsertSavings -> savingsRepository.upsertSavings(event.savings)
            is SavingsEvent.DeleteSavings -> savingsRepository.deleteSavings(event.savingsId)
        }
    }

    private fun setUpSavingsJob() {
        savingsJob?.cancel()
        savingsJob = viewModelScope.launch(Dispatchers.IO) {
            savingsRepository.getSavingsFlow().collect { savings ->
                _state.update { it.copy(total = savings.sumOf { it.amount }.toFloat(), savings = savings) }
            }
        }
    }

}