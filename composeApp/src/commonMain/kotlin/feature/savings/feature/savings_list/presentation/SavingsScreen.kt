package feature.savings.feature.savings_list.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import data.repository.AppPreferences
import feature.savings.feature.savings_list.presentation.component.SavingsTabRow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.goal_with_value
import moinobudget.composeapp.generated.resources.register_savings
import moinobudget.composeapp.generated.resources.register_savings_details
import moinobudget.composeapp.generated.resources.total_savings
import org.jetbrains.compose.resources.stringResource
import presentation.dashedBorder
import presentation.data.IncomeOrOutcome
import presentation.data.SavingsType
import presentation.data.SavingsUI
import presentation.formatCurrency

@Composable
fun SavingsScreen(
    state: SavingsState,
    preferences: AppPreferences,
    addEditSavings: (Int?, SavingsType) -> Unit,
    onEvent: (SavingsEvent) -> Unit,
) {
    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(initialPage = 1, pageCount = { SavingsType.entries.size+1 })

    var addSavingsDialog by remember { mutableStateOf(false) }
    var savingsForDialog by remember { mutableStateOf<SavingsUI?>(null) }

    /*if (addSavingsDialog) AddEditSavingsDialog(
        savings = savingsForDialog,
        preferences = preferences,
        saveSavings = { onEvent(SavingsEvent.UpsertSavings(it)) },
        deleteSavings = { onEvent(SavingsEvent.DeleteSavings(it)) },
        defaultSavings = SavingsType.entries.getOrElse(pagerState.currentPage-1) { SavingsType.SavingsBooks },
        onDismiss = { addSavingsDialog = false; savingsForDialog = null }
    )*/

    Column {
        Column(Modifier.padding(start = 24.dp, top = 16.dp)) {
            Crossfade(modifier = Modifier.fillMaxWidth(),
                targetState =
                    (if (pagerState.currentPage == 0) state.savings
                    else state.savings.filter { it.type.id == pagerState.currentPage-1 })
                        .sumOf { it.amount }
            ) { total ->
                Text(formatCurrency(total.toFloat(), preferences.copy(decimalMode = true)),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium,
                    color = IncomeOrOutcome.Income.color)
            }
            Text(stringResource(Res.string.total_savings),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleMedium)
        }

        var selectedTabIndex by remember { mutableStateOf(1) }

        LaunchedEffect(pagerState.currentPage) { selectedTabIndex = pagerState.currentPage }

        SavingsTabRow(
            selectedTabPosition = selectedTabIndex,
            selectTab = {
                selectedTabIndex = it
                scope.launch { pagerState.animateScrollToPage(it) }
            }
        )

        HorizontalPager(pagerState,
            modifier = Modifier.weight(1f)) { page ->
                val listState = rememberLazyListState()
                Box(Modifier.fillMaxSize()) {
                    LazyColumn(state = listState,
                        modifier = Modifier
                            //.matchParentSize()
                            .padding(horizontal = 16.dp)) {
                        items(if (page == 0) state.savings else state.savings.filter { it.type.id == page-1 }) { savings ->
                            SavingsItem(modifier = Modifier,
                                savings = savings,
                                preferences = preferences,
                                onClick = { addEditSavings(
                                    savings.id,
                                    SavingsType.entries.getOrElse(pagerState.currentPage-1) { SavingsType.SavingsBooks }) }
                            )
                        }
                        item {
                            Row(Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp, bottom = 24.dp)
                                .dashedBorder(4.dp, MaterialTheme.colorScheme.primary, 16.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .clickable { addEditSavings(
                                    null,
                                    SavingsType.entries.getOrElse(pagerState.currentPage-1) { SavingsType.SavingsBooks }
                                ) }
                                .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.3f)),
                                    tint = MaterialTheme.colorScheme.primary,
                                    imageVector = Icons.Default.Add,
                                    contentDescription = stringResource(Res.string.register_savings_details))
                                Column(Modifier.padding(start = 16.dp)) {
                                    Text(stringResource(Res.string.register_savings),
                                        fontWeight = FontWeight.SemiBold,
                                        style = MaterialTheme.typography.titleMedium)
                                    Text(stringResource(Res.string.register_savings_details),
                                        fontWeight = FontWeight.Normal,
                                        style = MaterialTheme.typography.titleSmall)
                                }
                            }
                        }
                    }
                    Crossfade(modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth(),
                        targetState = listState.canScrollBackward) { displayDivider ->
                        if (displayDivider) HorizontalDivider(Modifier.fillMaxWidth(),
                            thickness = 2.dp)
                }
            }
        }
    }
}

@Composable
fun SavingsItem(
    modifier: Modifier,
    preferences: AppPreferences,
    savings: SavingsUI,
    onClick: () -> Unit
) = Box(modifier
    .fillMaxWidth()
    .padding(vertical = 8.dp)
    .clip(RoundedCornerShape(16.dp))
    .background(MaterialTheme.colorScheme.surface)
    .then(
        if (savings.goal != null && savings.amount >= savings.goal) Modifier.completeShimmer(savings.color ?: IncomeOrOutcome.Income.color)
        else Modifier)
    .clickable { onClick() }
    .padding(16.dp)
) {
    val savingPrimary = savings.color ?: MaterialTheme.colorScheme.primary

    Column(Modifier.padding(horizontal = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            savings.icon?.let { icon ->
                Icon(icon.icon,
                    modifier = Modifier.padding(end = 16.dp).size(32.dp),
                    contentDescription = null)
            }
            Column {
                Row {
                    Text(savings.title,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.SemiBold)
                    Text(
                        formatCurrency(savings.amount.toFloat(), preferences),
                        color = savingPrimary,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold)
                }
                Text(savings.subtitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold)
            }
        }

        savings.goal?.let {

            val currentProgress = remember { Animatable(0f) }
            val progress = savings.amount.toFloat() / savings.goal

            LaunchedEffect(key1 = progress) {
                delay(300)
                currentProgress.animateTo(
                    targetValue = progress,
                    animationSpec = tween(1500)
                )
            }

            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).height(6.dp),
                progress = { currentProgress.value },
                color = savingPrimary,
                trackColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                drawStopIndicator = {}
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("${(progress * 100).toInt()}%",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    color = savingPrimary,
                    style = MaterialTheme.typography.titleLarge)
                Text(
                    stringResource(Res.string.goal_with_value, formatCurrency(savings.goal.toFloat(), preferences)),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Light)
            }
        }
    }
}

fun Modifier.completeShimmer(mainColor: Color): Modifier = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val transition = rememberInfiniteTransition(label = "shimmer_effect_transition")
    val startOffsetX by transition.animateFloat(
        initialValue = -1.2f * size.width.toFloat(),
        targetValue = 1.2f * size.width.toFloat(),
        animationSpec = infiniteRepeatable(animation = tween(6000)), label = "shimmer_effect_animation"
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(Color.Transparent, mainColor, Color.Transparent),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    ).onGloballyPositioned { size = it.size }
}