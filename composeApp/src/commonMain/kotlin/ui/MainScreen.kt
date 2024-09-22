package ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import data.repository.AppPreferences
import feature.budgets.data.BudgetStyle
import feature.budgets.feature.add_edit_budget_operation.presentation.AddEditBudgetOperationScreen
import feature.budgets.feature.add_edit_budget_operation.presentation.AddEditBudgetOperationViewModel
import feature.expenses.feature.add_edit_envelope.presentation.AddEditEnvelopeScreen
import feature.expenses.feature.add_edit_envelope.presentation.AddEditEnvelopeViewModel
import feature.expenses.feature.add_edit_expense.AddEditExpenseScreen
import feature.expenses.feature.add_edit_expense.AddEditExpenseViewModel
import feature.expenses.feature.envelope_details.presentation.EnvelopeDetailsScreen
import feature.expenses.feature.envelope_details.presentation.EnvelopeDetailsViewModel
import feature.hub.HubScreen
import feature.savings.data.SavingsType
import feature.savings.feature.add_edit_savings.presentation.AddEditSavingsScreen
import feature.savings.feature.add_edit_savings.presentation.AddEditSavingsViewModel
import feature.savings.feature.savings_detail.SavingsDetailsScreen
import feature.savings.feature.savings_detail.SavingsDetailsViewModel
import feature.settings.SettingsScreen
import feature.settings.SettingsViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
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
            startDestination = MoinoBudgetScreen.Main
        ) {
            /** Main Screens **/
            composable<MoinoBudgetScreen.Main> {
                HubScreen(
                    preferences = preferences,
                    goToScreen = { navController.navigate(it) },
                    setStyle = { setStyle(it) }
                )
            }
            composable<MoinoBudgetScreen.Settings>(
                enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                exitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
            ) {
                val viewModel = koinViewModel<SettingsViewModel>()
                SettingsScreen(
                    goBack = { navController.popBackStack() },
                    preferences = preferences,
                    onEvent = viewModel::onEvent)
            }

            /** Regular Operations **/
            composable<MoinoBudgetScreen.AddEditBudgetOperation>(
                enterTransition = { slideInVertically(initialOffsetY = { it }) },
                exitTransition = { slideOutVertically(targetOffsetY = { it }) }
            ) {
                val args = it.toRoute<MoinoBudgetScreen.AddEditBudgetOperation>()
                val expenseId = args.expenseId
                val labels = args.labelIds

                val viewModel = koinViewModel<AddEditBudgetOperationViewModel> { parametersOf(expenseId, labels) }
                val state by viewModel.state.collectAsStateWithLifecycle()

                AddEditBudgetOperationScreen(
                    preferences = preferences,
                    state = state,
                    onEvent = viewModel::onEvent,
                    goBack = { navController.popBackStack() }
                )
            }

            /** Savings **/
            composable<MoinoBudgetScreen.AddEditSavings>(
                enterTransition = { slideInVertically(initialOffsetY = { it }) },
                exitTransition = { slideOutVertically(targetOffsetY = { it }) }
            ) {
                val args = it.toRoute<MoinoBudgetScreen.AddEditSavings>()
                val savingsId = args.savingsId
                val savingsType = SavingsType.findById(args.defaultSavingsTypeId)

                val viewModel = koinViewModel<AddEditSavingsViewModel> { parametersOf(savingsId) }
                val state by viewModel.state.collectAsStateWithLifecycle()

                AddEditSavingsScreen(
                    state = state,
                    preferences = preferences,
                    onEvent = viewModel::onEvent,
                    uiEvent = viewModel.eventFlow,
                    defaultSavings = savingsType,
                    goBack = { navController.popBackStack() },
                    deleteEntry = { navController.popBackStack(route = MoinoBudgetScreen.Main, inclusive = false) }
                )
            }
            composable<MoinoBudgetScreen.SavingsDetails>(
                enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
            ) {
                val args = it.toRoute<MoinoBudgetScreen.SavingsDetails>()
                val savingsId = args.savingsId

                val viewModel = koinViewModel<SavingsDetailsViewModel> { parametersOf(savingsId) }
                val state by viewModel.state.collectAsStateWithLifecycle()

                SavingsDetailsScreen(
                    state = state,
                    preferences = preferences,
                    onEvent = viewModel::onEvent,
                    goToEdit = { id, type -> navController.navigate(
                        MoinoBudgetScreen.AddEditSavings(savingsId = id, defaultSavingsTypeId = type.id)
                    ) },
                    goBack = { navController.popBackStack() }
                )
            }

            /** Envelopes & Expenses **/
            composable<MoinoBudgetScreen.AddEditEnvelope>(
                enterTransition = { slideInVertically(initialOffsetY = { it }) },
                popExitTransition = { slideOutVertically(targetOffsetY = { it }) },
                popEnterTransition = { EnterTransition.None }
            ) {
                val args = it.toRoute<MoinoBudgetScreen.AddEditEnvelope>()
                val envelopeId = args.envelopeId

                val viewModel = koinViewModel<AddEditEnvelopeViewModel> { parametersOf(envelopeId) }
                val state by viewModel.state.collectAsStateWithLifecycle()

                AddEditEnvelopeScreen(
                    state = state,
                    preferences = preferences,
                    onEvent = viewModel::onEvent,
                    uiEvent = viewModel.eventFlow,
                    goBack = { navController.popBackStack() },
                    deleteEntry = { navController.popBackStack(route = MoinoBudgetScreen.Main, inclusive = false) }
                )
            }
            composable<MoinoBudgetScreen.EnvelopeDetails>(
                enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
            ) {
                val args = it.toRoute<MoinoBudgetScreen.AddEditEnvelope>()
                val envelopeId = args.envelopeId

                val viewModel = koinViewModel<EnvelopeDetailsViewModel> { parametersOf(envelopeId) }
                val state by viewModel.state.collectAsStateWithLifecycle()

                EnvelopeDetailsScreen(
                    state = state,
                    preferences = preferences,
                    addEditEnvelope = { navController.navigate(MoinoBudgetScreen.AddEditEnvelope(it)) },
                    goBack = { navController.popBackStack() },
                    addExpense = { navController.navigate(MoinoBudgetScreen.AddEditExpense(-1, envelopeId)) }
                )
            }
            composable<MoinoBudgetScreen.AddEditExpense>(
                enterTransition = { slideInVertically(initialOffsetY = { it }) },
                exitTransition = { slideOutVertically(targetOffsetY = { it }) }
            ) {
                val args = it.toRoute<MoinoBudgetScreen.AddEditExpense>()
                val expenseId = args.expenseId
                val envelopeId = args.envelopeId

                val viewModel = koinViewModel<AddEditExpenseViewModel> { parametersOf(expenseId, envelopeId) }
                val state by viewModel.state.collectAsStateWithLifecycle()

                AddEditExpenseScreen(
                    state = state,
                    preferences = preferences,
                    onEvent = viewModel::onEvent,
                    goBack = { navController.popBackStack() },
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