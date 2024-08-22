package ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import data.repository.AppPreferences
import feature.add_edit_expense.presentation.AddEditExpenseScreen
import feature.add_edit_expense.presentation.AddEditExpenseViewModel
import feature.dashboard.presentation.DashboardScreen
import feature.dashboard.presentation.DashboardViewModel
import feature.settings.SettingsScreen
import feature.settings.SettingsViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import presentation.data.BudgetStyle
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
            startDestination = MoinoBudgetScreen.Dashboard,
        ) {
            composable<MoinoBudgetScreen.Dashboard> {
                val viewModel = koinViewModel<DashboardViewModel>()
                val state by viewModel.state.collectAsState()
                DashboardScreen(
                    preferences = preferences,
                    state = state,
                    onEvent = viewModel::onEvent,
                    uiEvent = viewModel.eventFlow,
                    goTo = { navController.navigate(it) })
            }
            composable<MoinoBudgetScreen.AddEditExpense>{
                val args = it.toRoute<MoinoBudgetScreen.AddEditExpense>()
                val style = BudgetStyle.findById(args.styleId)
                val labels = args.labelIds

                val viewModel = koinViewModel<AddEditExpenseViewModel>()
                val state by viewModel.state.collectAsState()
                AddEditExpenseScreen(
                    style = style,
                    labels = labels,
                    preferences = preferences,
                    state = state,
                    onEvent = viewModel::onEvent,
                    goBack = { navController.popBackStack() }
                )
            }
            composable<MoinoBudgetScreen.Settings> {
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