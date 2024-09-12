package ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import data.repository.AppPreferences
import feature.expenses.add_edit_expense.presentation.AddEditExpenseEvent
import feature.expenses.add_edit_expense.presentation.AddEditExpenseScreen
import feature.expenses.add_edit_expense.presentation.AddEditExpenseViewModel
import feature.hub.HubScreen
import feature.savings.feature.add_edit_savings.presentation.AddEditSavingsEvent
import feature.savings.feature.add_edit_savings.presentation.AddEditSavingsScreen
import feature.savings.feature.add_edit_savings.presentation.AddEditSavingsViewModel
import feature.settings.SettingsScreen
import feature.settings.SettingsViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import presentation.data.BudgetStyle
import presentation.data.SavingsType
import ui.theme.MoinoBudgetTheme

@Composable
fun MainScreen(
    preferences: AppPreferences,
    setStyle: (BudgetStyle) -> Unit
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
                    goToScreen = { navController.navigate(it) },
                    setStyle = { setStyle(it) }
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
                val expenseId = args.expenseId
                val labels = args.labelIds

                val viewModel = koinViewModel<AddEditExpenseViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()

                LaunchedEffect(true) {
                    viewModel.onEvent(
                        AddEditExpenseEvent.Init(
                        expenseId = if (expenseId == -1) null else expenseId,
                        labels = labels
                    ))
                }

                AddEditExpenseScreen(
                    preferences = preferences,
                    state = state,
                    onEvent = viewModel::onEvent,
                    goBack = { navController.popBackStack() }
                )
            }
            composable<MoinoBudgetScreen.AddEditSavings> {
                val args = it.toRoute<MoinoBudgetScreen.AddEditSavings>()
                val savingsId = args.savingsId
                val savingsType = SavingsType.findById(args.defaultSavingsTypeId)

                val viewModel = koinViewModel<AddEditSavingsViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()

                LaunchedEffect(true) {
                    viewModel.onEvent(AddEditSavingsEvent.Init(if (savingsId == -1) null else savingsId))
                }

                AddEditSavingsScreen(
                    state = state,
                    preferences = preferences,
                    onEvent = viewModel::onEvent,
                    defaultSavings = savingsType,
                    goBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() = MoinoBudgetTheme { MainScreen(
    preferences = AppPreferences(),
    setStyle = {}
) }