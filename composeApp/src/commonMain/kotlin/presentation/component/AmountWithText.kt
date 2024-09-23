package presentation.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import data.repository.AppPreferences
import presentation.data.IncomeOrOutcome
import presentation.formatCurrency

@Composable
fun AmountWithText(
    modifier: Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    amount: Int,
    text: String,
    preferences: AppPreferences,
    animation: AmountAnimation = AmountAnimation.Fading
) = Column(modifier,
    horizontalAlignment = horizontalAlignment) {
    when (animation) {
        AmountAnimation.Fading -> Crossfade(modifier = Modifier.fillMaxWidth(),
            targetState = amount
        ) { total ->
            Text(
                formatCurrency(total.toFloat(), preferences.copy(decimalMode = true)),
                modifier = if (horizontalAlignment == Alignment.CenterHorizontally) Modifier.fillMaxWidth(1f) else Modifier,
                fontWeight = FontWeight.Bold,
                textAlign = if (horizontalAlignment == Alignment.CenterHorizontally) TextAlign.Center else TextAlign.Start,
                style = MaterialTheme.typography.headlineMedium,
                color = if (total > 0) IncomeOrOutcome.Income.color else IncomeOrOutcome.Outcome.color)
        }
        AmountAnimation.Rolling -> AnimatedCounter(
            amount,
            formatCurrency(amount.toFloat(), preferences.copy(decimalMode = true))
        )
    }
    Text(
        text,
        fontWeight = FontWeight.SemiBold,
        style = MaterialTheme.typography.titleMedium)
}

@Composable
fun AnimatedCounter(
    amount: Int,
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.headlineMedium
) {
    var oldText by remember { mutableStateOf(text) }
    var oldAmount by remember { mutableStateOf(amount) }
    SideEffect { oldText = text; oldAmount = amount }

    val textColor: Color by animateColorAsState((if (amount > 0) IncomeOrOutcome.Income else IncomeOrOutcome.Outcome).color)

    Row(modifier = modifier.fillMaxWidth()) {
        for(i in text.indices) {
            val oldChar = oldText.getOrNull(i)
            val newChar = text[i]
            val char = if(oldChar == newChar) oldText[i] else text[i]
            AnimatedContent(
                targetState = char,
                transitionSpec = {
                    if (oldAmount < amount)
                        slideInVertically { it } togetherWith  slideOutVertically { -it }
                    else
                        slideInVertically { -it } togetherWith slideOutVertically { it }
                }
            ) { char ->
                Text(
                    text = char.toString(),
                    fontWeight = FontWeight.Bold,
                    style = style,
                    softWrap = false,
                    color = textColor
                )
            }
        }
    }
}

enum class AmountAnimation { Fading, Rolling }

@Composable
fun ShimmerAmountWithText(modifier: Modifier) = Column(modifier) {
    Text(
        "",
        modifier = Modifier.padding(bottom = 8.dp).width(72.dp).shimmerEffect(8.dp),
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.headlineMedium)
    Text(
        "",
        modifier = Modifier.width(96.dp).shimmerEffect(8.dp),
        fontWeight = FontWeight.SemiBold,
        style = MaterialTheme.typography.titleMedium)
}