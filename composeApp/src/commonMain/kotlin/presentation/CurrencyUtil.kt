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
    val amount = (if (preferences.decimalMode) {
        val rounded = ceil(value * 100) / 100
        if (rounded % 1.0 == 0.0) rounded.toInt().toString() // Convert to integer if there's no fractional part
        else rounded.toString() // Otherwise, return the rounded value as a string
    }
    else ceil(value).toInt().toString())

    // Split the string into the integer and fractional parts
    val parts = amount.split(".", ",")
    // Get the part before the decimal/comma
    val integerPart = parts[0]
    // Reverse the integer part to facilitate adding spaces every 3 characters
    val reversedIntegerPart = integerPart.reversed()
    // Add spaces every 3 characters
    val formattedReversed = reversedIntegerPart.chunked(3).joinToString(" ")
    // Reverse it back to get the final formatted integer part
    val formattedIntegerPart = formattedReversed.reversed()
    // If there is a fractional part, append it back with a "." or ","
    return if (parts.size > 1) "$formattedIntegerPart.${parts[1]}" else formattedIntegerPart
}