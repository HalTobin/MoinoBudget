package feature.dashboard.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import feature.dashboard.data.MonthYearWithData
import kotlinx.coroutines.launch
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.add_operation
import moinobudget.composeapp.generated.resources.app_name
import moinobudget.composeapp.generated.resources.available_in
import moinobudget.composeapp.generated.resources.due_in
import moinobudget.composeapp.generated.resources.go_to_settings_help
import moinobudget.composeapp.generated.resources.incomes
import moinobudget.composeapp.generated.resources.month
import moinobudget.composeapp.generated.resources.my_incomes
import moinobudget.composeapp.generated.resources.outcomes
import moinobudget.composeapp.generated.resources.upcoming_payments
import moinobudget.composeapp.generated.resources.year
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import presentation.data.ExpenseUI
import presentation.data.IncomeOrOutcome
import ui.Screen
import ui.theme.Orange80
import kotlin.math.roundToInt

@Composable
fun DashboardScreen(
    state: DashboardState,
    onEvent: (DashboardEvent) -> Unit,
    goTo: (Screen) -> Unit
) = Box {
    val scope = rememberCoroutineScope()
    IconButton(modifier = Modifier.align(Alignment.TopEnd).padding(8.dp),
        onClick = { goTo(Screen.Settings) }) {
        Icon(modifier = Modifier.size(32.dp),
            imageVector = Icons.Default.Settings, contentDescription = stringResource(Res.string.go_to_settings_help))
    }
    Column {
        Column(modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(stringResource(Res.string.app_name).uppercase(),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(16.dp))
            FinancialSummary(
                totalExpensesMonth = state.upcomingPaymentsMonthly,
                disposableIncomeMonth = state.disposableIncomesMonthly,
                totalExpensesYear = state.upcomingPaymentsAnnual,
                disposableIncomeYear = state.disposableIncomesAnnual
            )
            Spacer(modifier = Modifier.height(16.dp))
            RegisterOperation(onClick = {})
        }
        Column {
            val pagerState = rememberPagerState(pageCount = { 2 })
            DashboardTab(pagerState, onSelect = { scope.launch {
                pagerState.animateScrollToPage(it) }})
            Spacer(Modifier.height(8.dp))
            HorizontalPager(state = pagerState) { page ->
                if (page == IncomeOrOutcome.Outcome.tabId) PaymentsSection(IncomeOrOutcome.Outcome, state.expenses)
                else PaymentsSection(IncomeOrOutcome.Income, state.expenses)
            }
        }
    }
}

@Composable
fun DashboardTab(pagerState: PagerState,
                 onSelect: (tabId: Int) -> Unit) {
    TabRow(modifier = Modifier.fillMaxWidth(),
        selectedTabIndex = pagerState.currentPage,
        containerColor = MaterialTheme.colorScheme.background,
        tabs = {
            listOf(IncomeOrOutcome.Outcome, IncomeOrOutcome.Income).forEach { incomeOrOutcome ->
                Tab(
                    selected = (pagerState.currentPage == incomeOrOutcome.tabId),
                    onClick = { onSelect(incomeOrOutcome.tabId) },
                    text = { Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = incomeOrOutcome.icon, contentDescription = null)
                        Text(stringResource(incomeOrOutcome.text).uppercase(),
                            modifier = Modifier.padding(start = 8.dp))
                    } }
                )
            }
        },
        divider = { HorizontalDivider(thickness = 2.dp) }
    )
    Spacer(Modifier.height(8.dp))
}

@Composable
fun RegisterOperation(onClick: () -> Unit) = Button(onClick = onClick,
    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
    shape = RoundedCornerShape(8.dp),
    colors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    )
) {
    Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(Res.string.add_operation))
    Text(stringResource(Res.string.add_operation),
        modifier = Modifier.padding(horizontal = 8.dp),
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
fun FinancialSummary(
    totalExpensesMonth: Float,
    disposableIncomeMonth: Float,
    totalExpensesYear: Float,
    disposableIncomeYear: Float
) {
    var year by remember { mutableStateOf(false) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        BigValue(modifier = Modifier.weight(1f),
            yearWithData = MonthYearWithData(
                isYear = year,
                yearAmount = disposableIncomeYear,
                monthAmount = disposableIncomeMonth
            ),
            color = Color.Green,
            title = stringResource(Res.string.incomes)
        )
        BigValue(modifier = Modifier.weight(1f),
            yearWithData = MonthYearWithData(
                isYear = year,
                yearAmount = totalExpensesYear,
                monthAmount = totalExpensesMonth
            ),
            color = Color.Red,
            title = stringResource(Res.string.outcomes)
        )
        Box(Modifier
            .padding(end = 8.dp)
            .width(40.dp)
            .height(80.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { year = !year },
        ) {
            val pxToMove = with(LocalDensity.current) { 41.dp.toPx().roundToInt() }
            val offset by animateIntOffsetAsState(
                targetValue = if (!year) {
                    IntOffset(0, pxToMove)
                } else {
                    IntOffset.Zero
                },
                label = "offset"
            )
            Box(
                modifier = Modifier
                    .padding(top = 3.dp)
                    .offset { offset }
                    .size(32.dp)
                    .align(Alignment.TopCenter)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )
            Text(stringResource(Res.string.year).first().uppercase(),
                modifier = Modifier.padding(8.dp).align(Alignment.TopCenter),
                color = if (year) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold)
            Text(stringResource(Res.string.month).first().uppercase(),
                modifier = Modifier.padding(8.dp).align(Alignment.BottomCenter),
                color = if (!year) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun BigValue(modifier: Modifier, yearWithData: MonthYearWithData, color: Color, title: String) =
    Column(modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally) {
        AnimatedContent(targetState = yearWithData,
            transitionSpec = {
                if (!targetState.isYear) {
                    slideInVertically(initialOffsetY = { -it }, animationSpec = tween(300)) + fadeIn() togetherWith
                            slideOutVertically(targetOffsetY = { it }, animationSpec = tween(300)) + fadeOut()
                } else {
                    slideInVertically(initialOffsetY = { it }, animationSpec = tween(300)) + fadeIn() togetherWith
                            slideOutVertically(targetOffsetY = { -it }, animationSpec = tween(300)) + fadeOut()
                }
            }) { state ->
            Text("${ if (!state.isYear) state.monthAmount else state.yearAmount}",
                modifier = Modifier.fillMaxWidth(),
                color = color,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.displaySmall)
        }
        Text(title.uppercase(), fontWeight = FontWeight.Bold, fontSize = 18.sp)
}

@Composable
fun PaymentsSection(incomeOrOutcome: IncomeOrOutcome,
    dueExpenses: List<ExpenseUI>) = Column(modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally) {
    Text(stringResource(if (incomeOrOutcome == IncomeOrOutcome.Outcome) Res.string.upcoming_payments else Res.string.my_incomes),
        fontSize = 20.sp, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(8.dp))
    LazyColumn(Modifier.weight(1f)) {
        items(dueExpenses.filter { it.type == incomeOrOutcome }.sortedBy { it.dueIn }) { dueExpense ->
            DueExpenseItem(dueExpense)
        }
    }
}

@Composable
fun DueExpenseItem(dueExpense: ExpenseUI) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .clip(RoundedCornerShape(8.dp))
        .background(MaterialTheme.colorScheme.surface)
        .padding(8.dp),
    verticalAlignment = Alignment.CenterVertically
) {
    Icon(modifier = Modifier.padding(horizontal = 8.dp).size(32.dp),
        imageVector = dueExpense.icon.icon, contentDescription = null)

    Column(Modifier.padding(start = 4.dp)) {
        Row {
            Text(dueExpense.title, modifier = Modifier.weight(1f), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text("${if (dueExpense.type == IncomeOrOutcome.Outcome) "-" else "+"}${dueExpense.amount}$",
                textAlign = TextAlign.End,
                color = if (dueExpense.type == IncomeOrOutcome.Outcome) Color.Red else Color.Green,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            fun getColorFromDueInDays(dueIn: Int): Color = if (dueIn < 7) {
                if (dueExpense.type == IncomeOrOutcome.Outcome) Color.Red else Color.Green
            } else if (dueIn < 30) Orange80 else Color.Green
            val dueInText = pluralStringResource(
                if (dueExpense.type == IncomeOrOutcome.Outcome) Res.plurals.due_in else Res.plurals.available_in,
                dueExpense.dueIn, dueExpense.dueIn)
            val numberText = dueExpense.dueIn.toString()
            val styledText = buildAnnotatedString {
                val splitText = dueInText.split(numberText)
                append(splitText[0])
                withStyle(style = SpanStyle(color = getColorFromDueInDays(dueExpense.dueIn),
                    fontWeight = FontWeight.SemiBold)) { append(numberText) }
                append(splitText[1])
            }
            Text(styledText, fontSize = 14.sp, modifier = Modifier.weight(1f))
            dueExpense.labels.forEach {
                Box(Modifier.padding(horizontal = 4.dp).size(20.dp).clip(CircleShape).background(it))
            }
        }
    }
}