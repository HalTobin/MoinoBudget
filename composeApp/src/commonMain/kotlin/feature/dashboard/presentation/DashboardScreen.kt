package feature.dashboard.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.repository.AppPreferences
import feature.dashboard.data.annual
import feature.dashboard.data.monthly
import feature.dashboard.presentation.component.TextSwitch
import kotlinx.coroutines.launch
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.operation
import moinobudget.composeapp.generated.resources.available_in
import moinobudget.composeapp.generated.resources.budget
import moinobudget.composeapp.generated.resources.disposable_dd
import moinobudget.composeapp.generated.resources.due_in
import moinobudget.composeapp.generated.resources.go_to_settings_help
import moinobudget.composeapp.generated.resources.my_incomes
import moinobudget.composeapp.generated.resources.payments_dd
import moinobudget.composeapp.generated.resources.to_put_aside_dd
import moinobudget.composeapp.generated.resources.upcoming_payments
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import presentation.LabelBackground
import presentation.data.BudgetUI
import presentation.data.ExpenseUI
import presentation.data.IncomeOrOutcome
import presentation.formatCurrency
import ui.Screen
import ui.theme.Orange80

@Composable
fun DashboardScreen(
    preferences: AppPreferences,
    state: DashboardState,
    onEvent: (DashboardEvent) -> Unit,
    goTo: (Screen) -> Unit
) = Box {
    val scope = rememberCoroutineScope()
    var year by remember { mutableStateOf(false) }


    Column {
        Row(modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically) {
            IconButton(modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface),
                onClick = { goTo(Screen.Settings) }) {
                Icon(modifier = Modifier.size(32.dp),
                    imageVector = Icons.Default.Savings, contentDescription = stringResource(Res.string.go_to_settings_help))
            }
            Spacer(Modifier.weight(1f))
            YearMonthSwitch(year, onChange = { year = it })
            Spacer(Modifier.weight(1f))
            IconButton(modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface),
                onClick = { goTo(Screen.Settings) }) {
                Icon(modifier = Modifier.size(32.dp),
                    imageVector = Icons.Default.Settings, contentDescription = stringResource(Res.string.go_to_settings_help))
            }
        }
        Column(modifier = Modifier.padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(8.dp))
            FinancialSummary(preferences = preferences,
                year = year,
                budget = state.budgets.first())
            Spacer(modifier = Modifier.height(16.dp))
            //RegisterOperation(onClick = {})
            QuickActions({}, {})
        }
        Spacer(Modifier.height(8.dp))
        Column {
            val pagerState = rememberPagerState(pageCount = { 2 })
            DashboardTab(pagerState, onSelect = { scope.launch {
                pagerState.animateScrollToPage(it) }})
            Spacer(Modifier.height(8.dp))
            HorizontalPager(state = pagerState) { page ->
                if (page == IncomeOrOutcome.Outcome.tabId) PaymentsSection(preferences, IncomeOrOutcome.Outcome, state.budgets.first().upcomingPayments.first, state.budgets.first().expenses)
                else PaymentsSection(preferences, IncomeOrOutcome.Income, state.budgets.first().rawIncomes.first, state.budgets.first().expenses)
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
    Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(Res.string.operation))
    Text(stringResource(Res.string.operation),
        modifier = Modifier.padding(horizontal = 8.dp),
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
fun QuickActions(
    createBudget: () -> Unit,
    addOperation: () -> Unit
) = Row {
    QuickActionButton(
        modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
        title = stringResource(Res.string.budget),
        icon = Icons.Default.Wallet,
        onClick = createBudget)
    QuickActionButton(
        modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
        title = stringResource(Res.string.operation),
        icon = Icons.Default.SyncAlt,
        onClick = addOperation)
}

@Composable
fun QuickActionButton(
    modifier: Modifier,
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) = Button(modifier = modifier,
    onClick = onClick,
    shape = CircleShape
) {
    Icon(imageVector = icon, contentDescription = title)
    Text(title,
        modifier = Modifier.padding(horizontal = 8.dp),
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
fun YearMonthSwitch(
    year: Boolean,
    onChange: (Boolean) -> Unit
) = TextSwitch(
    modifier = Modifier.width(256.dp),
    selectedIndex = if (year) 1 else 0,
    onSelectionChange = { onChange(!year) }
)

@Composable
fun FinancialSummary(
    year: Boolean,
    preferences: AppPreferences,
    budget: BudgetUI,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Card(modifier = Modifier
            .padding(horizontal = 32.dp)
            .weight(1f)
            .height(164.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(32.dp)
        ) {
            Box {
                LabelBackground(modifier = Modifier.fillMaxSize(), background = budget.style.background)
                Column(Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text("My budget".uppercase(),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(8.dp))
                    Text(stringResource(Res.string.disposable_dd))
                    MonthYearText(
                        preferences = preferences,
                        isYear = year,
                        values = budget.disposableIncomes,
                        textStyle = MaterialTheme.typography.titleLarge,
                        incomeOrOutcome = IncomeOrOutcome.Income)
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text(stringResource(Res.string.payments_dd),
                                style = MaterialTheme.typography.titleSmall)
                            MonthYearText(
                                preferences = preferences,
                                isYear = year,
                                values = budget.monthPayments,
                                textStyle = MaterialTheme.typography.titleSmall,
                                incomeOrOutcome = IncomeOrOutcome.Outcome)
                        }
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text(stringResource(Res.string.to_put_aside_dd),
                                style = MaterialTheme.typography.titleSmall)
                            MonthYearText(
                                preferences = preferences,
                                isYear = year,
                                values = budget.toPutAside,
                                textStyle = MaterialTheme.typography.titleSmall,
                                incomeOrOutcome = IncomeOrOutcome.Outcome)
                        }
                    }
                }
                Column(Modifier
                    .padding(end = 8.dp)
                    .align(Alignment.CenterEnd)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))
                    .padding(4.dp)
                ) {
                    budget.labels.forEach { label ->
                        Box(Modifier.padding(vertical = 2.dp)
                            .size(20.dp).clip(CircleShape).background(label.color))
                    }
                }
            }
        }
    }
}

@Composable
fun MonthYearText(
    preferences: AppPreferences,
    modifier: Modifier = Modifier,
    isYear: Boolean,
    values: Pair<Float, Float>,
    incomeOrOutcome: IncomeOrOutcome,
    textStyle: TextStyle
) {
    val yearWithData = Pair(isYear, values)
    AnimatedContent(modifier = modifier,
        targetState = yearWithData,
        transitionSpec = {
            if (!targetState.first) {
                slideInVertically(initialOffsetY = { -it }, animationSpec = tween(300)) + fadeIn() togetherWith
                        slideOutVertically(targetOffsetY = { it }, animationSpec = tween(300)) + fadeOut()
            } else {
                slideInVertically(initialOffsetY = { it }, animationSpec = tween(300)) + fadeIn() togetherWith
                        slideOutVertically(targetOffsetY = { -it }, animationSpec = tween(300)) + fadeOut()
            }
        }) { state ->
        Text(formatCurrency(if (!state.first) state.second.monthly else state.second.annual, preferences),
            color = incomeOrOutcome.color,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            style = textStyle)
    }
}

@Composable
fun PaymentsSection(
    preferences: AppPreferences,
    incomeOrOutcome: IncomeOrOutcome,
    amount: Float,
    dueExpenses: List<ExpenseUI>
) = Column(modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally) {
    Row(Modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Text(stringResource(if (incomeOrOutcome == IncomeOrOutcome.Outcome) Res.string.upcoming_payments else Res.string.my_incomes),
            modifier = Modifier.weight(1f),
            fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(
            formatCurrency(amount, preferences),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = incomeOrOutcome.color)
    }
    Spacer(modifier = Modifier.height(8.dp))
    LazyColumn(Modifier.weight(1f)) {
        items(dueExpenses.filter { it.type == incomeOrOutcome }.sortedBy { it.dueIn }) { dueExpense ->
            DueExpenseItem(modifier = Modifier.animateItem(),
                preferences = preferences,
                dueExpense = dueExpense)
        }
    }
}

@Composable
fun DueExpenseItem(
    modifier: Modifier,
    preferences: AppPreferences,
    dueExpense: ExpenseUI) = Row(
    modifier = modifier
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
            Text("${if (dueExpense.type == IncomeOrOutcome.Outcome) "-" else "+"}${formatCurrency(dueExpense.amount, preferences)}",
                textAlign = TextAlign.End,
                color = dueExpense.type.color,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            fun getColorFromDueInDays(dueIn: Int): Color = if (dueIn < 7) {
                if (dueExpense.type == IncomeOrOutcome.Outcome) Color.Red else Color.Green
            } else if (dueIn < 30) Orange80 else Color.Green
            val dueInText = stringResource(dueExpense.frequency.title) + ", " + pluralStringResource(
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