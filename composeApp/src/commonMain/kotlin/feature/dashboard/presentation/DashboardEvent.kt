package feature.dashboard.presentation

import presentation.data.LabelUI

sealed class DashboardEvent {
    data class UpsertLabel(val label: LabelUI): DashboardEvent()
}