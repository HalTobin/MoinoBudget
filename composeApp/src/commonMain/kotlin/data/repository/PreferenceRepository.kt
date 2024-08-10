package data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import data.value.Currency
import data.value.Language
import data.value.PrefDefault
import data.value.PrefKey
import data.value.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import presentation.data.BudgetStyle

class PreferenceRepositoryImpl(
    private val prefs: DataStore<Preferences>
): PreferenceRepository {

    override suspend fun setCurrency(value: String) =
        updateStringPreference(PrefKey.CURRENCY, value)

    /*override suspend fun setDecimalMode(value: Boolean) =
        updateBooleanPreference(PrefKey.DECIMAL_MODE, value)*/

    override suspend fun setTheme(value: String) =
        updateStringPreference(PrefKey.THEME, value)

    override suspend fun setLanguage(value: String) =
        updateStringPreference(PrefKey.LANGUAGE, value)

    override suspend fun getPreferences(): AppPreferences =
        prefs.appPreferences.first()

    override val preferences: Flow<AppPreferences>
        get() = prefs.appPreferences

    private suspend fun updateBooleanPreference(key: String, value: Boolean) {
        prefs.edit { dataStore ->
            val dataStoreKey = booleanPreferencesKey(key)
            dataStore[dataStoreKey] = value
        }
    }

    private suspend fun updateIntPreference(key: String, value: Int) {
        prefs.edit { dataStore ->
            val dataStoreKey = intPreferencesKey(key)
            dataStore[dataStoreKey] = value
        }
    }

    private suspend fun updateStringPreference(key: String, value: String) {
        prefs.edit { dataStore ->
            val dataStoreKey = stringPreferencesKey(key)
            dataStore[dataStoreKey] = value
        }
    }

}

val DataStore<Preferences>.appPreferences get() = this.data.map { dataStore ->
    AppPreferences(
        currency = dataStore.getStringPreference(PrefKey.CURRENCY, PrefDefault.CURRENCY).toCurrency(),
        decimalMode = dataStore.getBooleanPreference(PrefKey.DECIMAL_MODE, true),
        theme = dataStore.getStringPreference(PrefKey.THEME, PrefDefault.THEME).toTheme(),
        language = dataStore.getStringPreference(PrefKey.LANGUAGE, PrefDefault.LANGUAGE).toLanguage()
    )
}

fun String.toCurrency(): Currency = when (this) {
    Currency.Euro.key -> Currency.Euro
    Currency.Dollar.key -> Currency.Dollar
    Currency.Ruble.key -> Currency.Ruble
    Currency.YenYuan.key -> Currency.YenYuan
    Currency.Pound.key -> Currency.Pound
    Currency.Lira.key -> Currency.Lira
    else -> Currency.Euro
}

fun String.toTheme(): Theme = when (this) {
    Theme.Dark.key -> Theme.Dark
    Theme.Light.key -> Theme.Light
    Theme.DarkOled.key -> Theme.DarkOled
    else -> Theme.Dark
}

fun Int.toLabelStyle(): BudgetStyle = when (this) {
    BudgetStyle.CitrusJuice.id -> BudgetStyle.CitrusJuice
    BudgetStyle.Winter.id -> BudgetStyle.Winter
    BudgetStyle.Wave.id -> BudgetStyle.Wave
    else -> BudgetStyle.CitrusJuice
}

fun String.toLanguage(): Language = when (this) {
    Language.English.key -> Language.English
    Language.French.key -> Language.French
    Language.Spanish.key -> Language.Spanish
    Language.Russian.key -> Language.Russian
    else -> Language.English
}

fun Preferences.getBooleanPreference(key: String, default: Boolean): Boolean {
    val dataStoreKey = booleanPreferencesKey(key)
    return this[dataStoreKey] ?: default
}

fun Preferences.getIntPreference(key: String, default: Int): Int {
    val dataStoreKey = intPreferencesKey(key)
    return this[dataStoreKey] ?: default
}

fun Preferences.getStringPreference(key: String, default: String): String {
    val dataStoreKey = stringPreferencesKey(key)
    return this[dataStoreKey] ?: default
}

interface PreferenceRepository {
    suspend fun setCurrency(value: String)
    //suspend fun setDecimalMode(value: Boolean)
    suspend fun setTheme(value: String)
    suspend fun setLanguage(value: String)

    suspend fun getPreferences(): AppPreferences
    val preferences: Flow<AppPreferences>
}

data class AppPreferences(
    val currency: Currency = Currency.Euro,
    val decimalMode: Boolean = true,
    val theme: Theme = Theme.Dark,
    val language: Language = Language.English
)