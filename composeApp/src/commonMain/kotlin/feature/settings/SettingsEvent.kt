package feature.settings

sealed class SettingsEvent {
    data class ChangeCurrency(val value: String): SettingsEvent()
    data class ChangeDecimalMode(val value: Boolean): SettingsEvent()
    data class ChangeTheme(val value: String): SettingsEvent()
    data class ChangeCardStyle(val value: Int): SettingsEvent()
    data class ChangeLanguage(val value: String): SettingsEvent()
}