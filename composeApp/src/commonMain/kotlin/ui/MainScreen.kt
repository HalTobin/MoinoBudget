package ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import data.repository.AppPreferences
import feature.settings.SettingsScreen
import feature.settings.SettingsViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import ui.theme.MoinoBudgetTheme

@OptIn(KoinExperimentalAPI::class)
@Composable
fun MainScreen(
    preferences: AppPreferences
) {
    val navController = rememberNavController()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Settings.route, // TODO - Make HomeScreen
        ) {
            composable(Screen.Home.route) {
                /*val viewModel = koinViewModel<HomeViewModel>()
                val state by viewModel.state.collectAsState()
                HomeScreen(
                    state = state,
                    onEvent = viewModel::onEvent,
                    goTo = { navController.navigate(it.route) })*/
            }
            composable(Screen.Settings.route) {
                val viewModel = koinViewModel<SettingsViewModel>()
                SettingsScreen(
                    goBack = { navController.popBackStack() },
                    preferences = preferences,
                    onEvent = viewModel::onEvent)
            }
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() = MoinoBudgetTheme { MainScreen(AppPreferences()) }