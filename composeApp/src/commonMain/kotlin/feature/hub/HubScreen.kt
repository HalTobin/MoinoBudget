package feature.hub

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import data.repository.AppPreferences
import feature.budgets.feature.budgets_list.presentation.DashboardScreen
import feature.budgets.feature.budgets_list.presentation.DashboardViewModel
import feature.savings.feature.savings_list.presentation.SavingsScreen
import feature.savings.feature.savings_list.presentation.SavingsViewModel
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.budgets_tab
import moinobudget.composeapp.generated.resources.expenses_tab
import moinobudget.composeapp.generated.resources.go_to_settings_help
import moinobudget.composeapp.generated.resources.savings_tab
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import feature.budgets.data.BudgetStyle
import feature.expenses.feature.envelope_list.presentation.EnvelopeScreen
import feature.expenses.feature.envelope_list.presentation.EnvelopeViewModel
import ui.MoinoBudgetScreen

@Composable
fun HubScreen(
    preferences: AppPreferences,
    goToScreen: (MoinoBudgetScreen) -> Unit,
    setStyle: (BudgetStyle) -> Unit
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            NavigationBar {
                HubScreenTab.entries.forEach { tab ->
                    val selected = currentDestination?.hierarchy?.any { it.route == tab.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            if (!selected) navController.navigate(tab.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        },
                        icon = {
                            Crossfade(targetState = selected) { selected ->
                                Icon(imageVector = if (selected) tab.icon.first else tab.icon.second, contentDescription = null)
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        ),
                        label = { Text(stringResource(tab.title)) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            color = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ) {
            Box {
                FloatingActionButton(
                    modifier = Modifier.align(Alignment.TopEnd).padding(8.dp).zIndex(2f).size(44.dp),
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    onClick = { goToScreen(MoinoBudgetScreen.Settings) }
                ) {
                    Icon(modifier = Modifier.size(32.dp),
                        imageVector = Icons.Default.Settings,
                        contentDescription = stringResource(Res.string.go_to_settings_help))
                }
                NavHost(modifier = Modifier.fillMaxSize(),
                    startDestination = HubScreenTab.Budget.route,
                    navController = navController) {
                    composable(HubScreenTab.Savings.route) {
                        val viewModel = koinViewModel<SavingsViewModel>()
                        val state by viewModel.state.collectAsStateWithLifecycle()
                        SavingsScreen(
                            state = state,
                            preferences = preferences,
                            addEditSavings = { savingsId, savingsType ->
                                goToScreen(MoinoBudgetScreen.AddEditSavings(
                                    savingsId = savingsId ?: -1,
                                    defaultSavingsTypeId = savingsType.id
                                )) },
                            goToDetails = { goToScreen(MoinoBudgetScreen.SavingsDetails(it)) },
                        )
                    }
                    composable(HubScreenTab.Budget.route) {
                        val viewModel = koinViewModel<DashboardViewModel>()
                        val state by viewModel.state.collectAsStateWithLifecycle()
                        DashboardScreen(
                            preferences = preferences,
                            state = state,
                            onEvent = viewModel::onEvent,
                            uiEvent = viewModel.eventFlow,
                            setStyle = { setStyle(it) },
                            goTo = { goToScreen(it) })
                    }
                    composable(HubScreenTab.Expenses.route) {
                        val viewModel = koinViewModel<EnvelopeViewModel>()
                        val state by viewModel.state.collectAsStateWithLifecycle()
                        EnvelopeScreen(
                            state = state,
                            preferences = preferences,
                            addEditEnvelope = { goToScreen(MoinoBudgetScreen.AddEditEnvelope(-1)) },
                            goToDetails = { goToScreen(MoinoBudgetScreen.EnvelopeDetails(it)) }
                        )
                    }
                }
            }
        }
    }

}

enum class HubScreenTab(
    val title: StringResource,
    val icon: Pair<ImageVector, ImageVector>,
    val route: String
) {
    Savings(title = Res.string.savings_tab, icon = Pair(Icons.Default.Savings, Icons.Outlined.Savings), route = "tab_savings"),
    Budget(title = Res.string.budgets_tab, icon = Pair(Icons.Default.Wallet, Icons.Outlined.Wallet), route = "tab_budget"),
    Expenses(title = Res.string.expenses_tab, icon = Pair(Icons.Default.CreditCard, Icons.Outlined.CreditCard), route = "tab_expenses")
}