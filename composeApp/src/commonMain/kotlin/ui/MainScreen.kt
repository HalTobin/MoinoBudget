package ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import data.repository.AppPreferences
import feature.add_edit_expense.presentation.AddEditExpenseEvent
import feature.add_edit_expense.presentation.AddEditExpenseScreen
import feature.add_edit_expense.presentation.AddEditExpenseViewModel
import feature.dashboard.presentation.DashboardScreen
import feature.dashboard.presentation.DashboardViewModel
import feature.hub.HubScreen
import feature.savings.presentation.SavingsScreen
import feature.savings.presentation.SavingsViewModel
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
            startDestination = MoinoBudgetScreen.Main,
        ) {
            composable<MoinoBudgetScreen.Main> {
                HubScreen(
                    preferences = preferences,
                    goToScreen = { navController.navigate(it) }
                )
            }
            composable<MoinoBudgetScreen.Settings> {
                val viewModel = koinViewModel<SettingsViewModel>()
                SettingsScreen(
                    goBack = { navController.popBackStack() },
                    preferences = preferences,
                    onEvent = viewModel::onEvent)
            }
            composable<MoinoBudgetScreen.AddEditExpense>{
                val args = it.toRoute<MoinoBudgetScreen.AddEditExpense>()
                val style = BudgetStyle.findById(args.styleId)
                val expenseId = args.expenseId
                val labels = args.labelIds

                val viewModel = koinViewModel<AddEditExpenseViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()

                LaunchedEffect(true) {
                    viewModel.onEvent(AddEditExpenseEvent.Init(
                        expenseId = if (expenseId == -1) null else expenseId,
                        labels = labels
                    ))
                }

                AddEditExpenseScreen(
                    style = style,
                    preferences = preferences,
                    state = state,
                    onEvent = viewModel::onEvent,
                    goBack = { navController.popBackStack() }
                )
            }

        }
    }
}

@Preview
@Composable
fun MainScreenPreview() = MoinoBudgetTheme { MainScreen(AppPreferences()) }