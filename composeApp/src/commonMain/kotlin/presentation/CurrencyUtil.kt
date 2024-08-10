package presentation

import data.repository.AppPreferences
import data.value.CurrencyPosition
import kotlin.math.ceil

fun formatCurrency(value: Float, preferences: AppPreferences): String {
    val currency = preferences.currency
    return when (currency.position) {
        CurrencyPosition.Start -> "${currency.sign}${formatNumber(value, preferences)}"
        CurrencyPosition.End -> "${formatNumber(value, preferences)} ${currency.sign}"
    }
}

fun formatNumber(value: Float, preferences: AppPreferences): String {
    return (if (preferences.decimalMode) {
        val rounded = ceil(value * 100) / 100
        return if (rounded % 1.0 == 0.0) {
            rounded.toInt().toString() // Convert to integer if there's no fractional part
        } else {
            rounded.toString() // Otherwise, return the rounded value as a string
        }
    }
    else ceil(value).toString())
}