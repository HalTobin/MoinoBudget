package ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.repository.AppPreferences
import data.repository.BudgetRepository
import data.repository.LabelRepository
import data.repository.PreferenceRepository
import feature.expenses.expenses_list.presentation.data.AddEditBudget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.default_budget
import org.jetbrains.compose.resources.getString
import presentation.data.BudgetStyle
import presentation.data.LabelUI

class MainViewModel(
    preferenceRepository: PreferenceRepository,
    labelRepository: LabelRepository,
    budgetRepository: BudgetRepository
): ViewModel() {

    private var preferencesJob: Job? = null

    private val _preferences = MutableStateFlow(AppPreferences())
    val preferences = _preferences.asStateFlow()

    init {
        preferencesJob?.cancel()
        preferencesJob = viewModelScope.launch(Dispatchers.IO) {
            preferenceRepository.preferences.collect { preferences ->
                _preferences.update { preferences }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            val labels = labelRepository.getLabels()
            if (labels.isEmpty()) {
                labelRepository.upsertLabels(LabelUI.defaults)
            }
            val budgets = budgetRepository.getBudgets()
            if (budgets.isEmpty()) {
                val defaultBudget = AddEditBudget(
                    id = null,
                    orderIndex = 0,
                    title = getString(Res.string.default_budget),
                    style = BudgetStyle.CitrusJuice,
                    labels = emptyList()
                )
                budgetRepository.upsertBudget(defaultBudget)
            }
        }
    }

}