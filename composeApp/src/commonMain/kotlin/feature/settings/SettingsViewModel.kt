package feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.repository.PreferenceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferenceRepository: PreferenceRepository
): ViewModel() {

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.ChangeLanguage -> viewModelScope.launch(Dispatchers.IO) {
                preferenceRepository.setLanguage(event.value)
            }
            is SettingsEvent.ChangeTheme -> viewModelScope.launch(Dispatchers.IO) {
                preferenceRepository.setTheme(event.value)
            }
        }
    }

}