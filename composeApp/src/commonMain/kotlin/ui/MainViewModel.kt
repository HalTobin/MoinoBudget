package ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.repository.AppPreferences
import data.repository.LabelRepository
import data.repository.PreferenceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import presentation.data.LabelUI

class MainViewModel(
    preferenceRepository: PreferenceRepository,
    labelRepository: LabelRepository
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
        }
    }

}