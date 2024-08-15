package feature.dashboard.presentation.dialog

import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import data.repository.AppPreferences
import feature.dashboard.presentation.data.AddEditBudget
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.close_dialog_description
import moinobudget.composeapp.generated.resources.delete_budget
import moinobudget.composeapp.generated.resources.edit_budget
import moinobudget.composeapp.generated.resources.labels
import moinobudget.composeapp.generated.resources.new_budget
import moinobudget.composeapp.generated.resources.save
import moinobudget.composeapp.generated.resources.title
import org.jetbrains.compose.resources.stringResource
import presentation.BudgetBackground
import presentation.data.BudgetStyle
import presentation.data.BudgetUI
import presentation.data.LabelUI
import presentation.pager.shake

@Composable
fun NewEditBudgetDialog(
    preferences: AppPreferences,
    budget: BudgetUI?,
    labels: List<LabelUI>,
    onDismiss: () -> Unit,
    saveBudget: (AddEditBudget) -> Unit,
    deleteBudget: (Int) -> Unit
) = Dialog(onDismissRequest = onDismiss) {
    val styles = BudgetStyle.list

    var style by remember { mutableStateOf(budget?.style ?: BudgetStyle.RedWaves) }

    val primary = remember { Animatable(if (preferences.theme.isDark) style.primary.second else style.primary.first) }
    val onPrimary = remember { Animatable(if (preferences.theme.isDark) style.onPrimary.second else style.onPrimary.first) }
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { styles.size })

    var deleteMode by remember { mutableStateOf(false) }

    var budgetTitle by remember { mutableStateOf(budget?.title ?: "") }
    val budgetLabels = remember { mutableStateListOf<Int>() }

    LaunchedEffect(key1 = true) {
        budget?.let { budget ->
            budgetLabels.addAll(budget.labels.map { it.id })
            pagerState.scrollToPage(styles.indexOf(budget.style))
        }
    }

    LaunchedEffect(key1 = pagerState.settledPage) {
        style = styles[pagerState.settledPage]
        primary.animateTo(if (preferences.theme.isDark) style.primary.second else style.primary.first)
        onPrimary.animateTo(if (preferences.theme.isDark) style.onPrimary.second else style.onPrimary.first)
    }

    Surface(
        color = MaterialTheme.colorScheme.background,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.surfaceVariant),
        contentColor = MaterialTheme.colorScheme.onBackground,
        shape = RoundedCornerShape(32.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp, top = 8.dp)) {
                budget?.let {
                    Crossfade(targetState = deleteMode) { deletion ->
                        IconButton(modifier = Modifier
                            .align(Alignment.CenterStart)
                            .clip(CircleShape)
                            .border(2.dp,
                                if (!deletion) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground,
                                CircleShape),
                            onClick = { deleteMode = !deleteMode }) {
                            Icon(imageVector = if (!deletion) Icons.Default.Delete else Icons.Default.Edit,
                                tint = if (!deletion) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground,
                                contentDescription = stringResource(Res.string.delete_budget))
                        }
                    }
                }
                Text(stringResource(budget?.let { Res.string.edit_budget } ?: Res.string.new_budget),
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.titleLarge,
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
                        .shake(deleteMode)
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
            Spacer(Modifier.height(8.dp))
            MaterialTheme(
                colorScheme = MaterialTheme.colorScheme.copy(
                    primary = primary.value,
                    onPrimary = onPrimary.value)
            ) {
                Column(Modifier.padding(bottom = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    var titleError by remember { mutableStateOf(false) }
                    TextField(modifier = Modifier.shake(deleteMode).clip(RoundedCornerShape(16.dp)),
                        isError = titleError,
                        value = budgetTitle,
                        onValueChange = {
                            if (titleError) titleError = false
                            if (it.length < 19) budgetTitle = it },
                        label = { Text(stringResource(Res.string.title)) },
                        colors = TextFieldDefaults.colors(
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.primary
                        ),
                        maxLines = 1,
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(stringResource(Res.string.labels),
                        modifier = Modifier.padding(start = 64.dp, top = 8.dp).fillMaxWidth(),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    LazyVerticalGrid(
                        GridCells.Fixed(4),
                        modifier = Modifier.padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(4.dp)
                    ) {
                        items(labels) { label ->
                            LabelEntry(label = label,
                                modifier = Modifier.animateItem().shake(deleteMode),
                                selectedLabels = budgetLabels,
                                onClick = {
                                    if (budgetLabels.any { it == label.id }) budgetLabels.remove(label.id)
                                    else budgetLabels.add(label.id)
                                },
                            )
                        }
                    }
                    AnimatedContent(modifier = Modifier.fillMaxWidth(),
                        targetState = deleteMode,
                        transitionSpec = {
                            slideInVertically(initialOffsetY = { it }) togetherWith slideOutVertically(targetOffsetY = { it })
                        }
                    ) { deletion ->
                        if (!deletion) Box(contentAlignment = Alignment.Center) {
                            Button(modifier = Modifier.padding(vertical = 4.dp),
                                shape = CircleShape,
                                onClick = {
                                    if (budgetTitle.isBlank()) titleError = true
                                    else {
                                        saveBudget(AddEditBudget(id = budget?.id, title = budgetTitle, style = style, labels = budgetLabels))
                                        onDismiss() } }
                            ) {
                                Icon(imageVector = Icons.Default.Save, contentDescription = stringResource(Res.string.save))
                                Text(stringResource(Res.string.save),
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    fontWeight = FontWeight.SemiBold)
                            }
                        }
                        else budget?.let {
                            TextButton(onClick = { deleteBudget(budget.id); onDismiss() }) {
                                Text(stringResource(Res.string.delete_budget),
                                    color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LabelEntry(
    modifier: Modifier,
    label: LabelUI,
    selectedLabels: List<Int>,
    onClick: () -> Unit
) {
    val selected = selectedLabels.any { it == label.id }
    Box(modifier = modifier
        .aspectRatio(1f)
        .padding(horizontal = 4.dp, vertical = 6.dp)
        .clip(RoundedCornerShape(8.dp))
        .background(
            if (selected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.surface)
        .clickable { onClick() }
        .padding(4.dp),
        contentAlignment = Alignment.Center) {
        Box(Modifier
            .size(30.dp)
            .clip(CircleShape)
            .border(2.dp, Color.Black, CircleShape)
            .background(label.color)
        )
    }
}