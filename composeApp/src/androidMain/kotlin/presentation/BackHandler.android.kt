package presentation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun NavBackHandler(enabled: Boolean, onBack: () -> Unit) = BackHandler(enabled) { onBack() }