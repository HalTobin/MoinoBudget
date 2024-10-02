package feature.expenses.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.repository.AppPreferences
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.available_until
import org.jetbrains.compose.resources.pluralStringResource
import presentation.formatCurrency

@Composable
fun RemainingMoney(
    remainingMoney: Int,
    remainingDays: Int,
    color: Color = MaterialTheme.colorScheme.onBackground,
    preferences: AppPreferences
) {
    val remainingMoneyFormatted = formatCurrency(remainingMoney.toFloat() / remainingDays, preferences)
    val fullText = pluralStringResource(Res.plurals.available_until, remainingDays, remainingMoneyFormatted, remainingDays)
    val styledText = buildAnnotatedString {
        val splitText = fullText.split(remainingMoneyFormatted, remainingDays.toString())
        val accentStyle = SpanStyle(
            fontSize = 18.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.SemiBold)
        append(splitText[0])
        withStyle(style = accentStyle) { append(remainingMoneyFormatted) }
        append(splitText[1])
        withStyle(style = accentStyle) { append(remainingDays.toString()) }
        append(splitText[2])
    }
    Text(styledText,
        modifier = Modifier.padding(bottom = 6.dp),
        color = color,
        fontStyle = FontStyle.Italic,
        fontSize = 16.sp)
}