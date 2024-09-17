package presentation.component

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import feature.budgets.feature.budgets_list.presentation.component.TextSwitch
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.month
import moinobudget.composeapp.generated.resources.year
import org.jetbrains.compose.resources.stringResource

@Composable
fun YearMonthSwitch(
    modifier: Modifier = Modifier,
    year: Boolean,
    onChange: (Boolean) -> Unit
) = TextSwitch(
    items = listOf(stringResource(Res.string.month), stringResource(Res.string.year)),
    modifier = modifier.width(256.dp),
    selectedIndex = if (year) 1 else 0,
    onSelectionChange = { onChange(!year) }
)