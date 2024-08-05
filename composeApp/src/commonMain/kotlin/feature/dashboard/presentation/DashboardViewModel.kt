package feature.dashboard.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DashboardViewModel(): ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    init {
        // Load expenses and payments...
    }

    fun onEvent(event: DashboardEvent) {
        when (event) {
            else -> {}
        }
    }

}