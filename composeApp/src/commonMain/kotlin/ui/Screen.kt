package ui

sealed class Screen(val route: String) {
    data object Dashboard: Screen("dashboard")
    data object AddEditExpense: Screen("add_edit_expense")
    data object Settings: Screen("setting")
}