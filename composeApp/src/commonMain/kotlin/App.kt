import androidx.compose.animation.Animatable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import presentation.data.BudgetStyle
import ui.MainScreen
import ui.MainViewModel
import ui.theme.MoinoBudgetTheme

@Composable
@Preview
fun App() = KoinContext {
    val scope = rememberCoroutineScope()

    val viewModel = koinViewModel<MainViewModel>()
    val preferences by viewModel.preferences.collectAsState()

    var style = BudgetStyle.CitrusJuice
    val primary = remember { Animatable(style.getPrimary(preferences)) }
    val onPrimary = remember { Animatable(style.getOnPrimary(preferences)) }

    MoinoBudgetTheme(
        primary = primary.value,
        onPrimary = onPrimary.value,
        theme = preferences.theme
    ) {
        MainScreen(
            preferences = preferences,
            setStyle = {
                scope.launch {
                    primary.animateTo(it.getPrimary(preferences))
                    onPrimary.animateTo(it.getOnPrimary(preferences))
                }
            }
        )
    }
}