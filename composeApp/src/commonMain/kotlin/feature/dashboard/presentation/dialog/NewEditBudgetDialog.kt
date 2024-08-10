package feature.dashboard.presentation.dialog

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import moinobudget.composeapp.generated.resources.edit_budget
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.close_dialog_description
import moinobudget.composeapp.generated.resources.new_budget
import moinobudget.composeapp.generated.resources.save
import org.jetbrains.compose.resources.stringResource
import presentation.BudgetBackground
import presentation.data.BudgetStyle

@Composable
fun NewEditBudgetDialog(
    onDismiss: () -> Unit,
    isEdition: Boolean = false,
    style: BudgetStyle = BudgetStyle.GrassAndSea
) = Dialog(onDismissRequest = onDismiss) {
    val styles = BudgetStyle.list
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { styles.size })

    var styleState by remember { mutableStateOf(style) }
    LaunchedEffect(key1 = pagerState.settledPage) { styleState = styles[pagerState.settledPage] }

    Surface(
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        shape = RoundedCornerShape(32.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp, top = 8.dp)) {
                Text(stringResource(if (isEdition) Res.string.edit_budget else Res.string.new_budget),
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold)
                IconButton(modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = stringResource(Res.string.close_dialog_description)) }
            }
            HorizontalPager(state = pagerState,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 32.dp),
            ) { page ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Card(modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .height(164.dp)
                        .aspectRatio(1.7f),
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.cardElevation(32.dp)
                    ) { BudgetBackground(Modifier.fillMaxSize(), styles[page].background) }
                    Text(stringResource(styles[page].title).uppercase(),
                        fontWeight = FontWeight.SemiBold,
                        fontStyle = FontStyle.Italic)
                }
            }

            Crossfade(targetState = styleState) { currentStyle ->
                MaterialTheme(
                    colorScheme = MaterialTheme.colorScheme.copy(
                        primary = currentStyle.primary.second,
                        onPrimary = currentStyle.onPrimary.second)
                    ) {
                    Column(Modifier.padding(bottom = 8.dp)) {
                        Button(onClick = {},
                            shape = CircleShape) {
                            Icon(imageVector = Icons.Default.Save, contentDescription = stringResource(Res.string.save))
                            Text(stringResource(Res.string.save),
                                modifier = Modifier.padding(horizontal = 8.dp),
                                fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}