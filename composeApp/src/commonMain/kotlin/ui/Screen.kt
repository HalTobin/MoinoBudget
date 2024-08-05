package ui

sealed class Screen(val route: String) {
    data object Dashboard: Screen("dashboard")
    data object Settings: Screen("setting")
}