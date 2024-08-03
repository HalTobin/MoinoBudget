package feature.settings

sealed class SettingsEvent {
    data class ChangeTheme(val value: String): SettingsEvent()
    data class ChangeLanguage(val value: String): SettingsEvent()
}