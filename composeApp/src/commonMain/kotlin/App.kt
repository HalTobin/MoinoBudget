import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import ui.MainScreen
import ui.MainViewModel
import ui.theme.MoinoBudgetTheme

@OptIn(KoinExperimentalAPI::class)
@Composable
@Preview
fun App() = KoinContext {
    val viewModel = koinViewModel<MainViewModel>()
    val preferences by viewModel.preferences.collectAsState()

    MoinoBudgetTheme(theme = preferences.theme) {
        MainScreen(preferences = preferences)
    }
}