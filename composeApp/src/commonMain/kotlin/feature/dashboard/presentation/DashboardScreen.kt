package feature.dashboard.presentation

import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Button
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import data.repository.AppPreferences
import feature.dashboard.data.annual
import feature.dashboard.data.monthly
import feature.dashboard.presentation.component.TextSwitch
import feature.dashboard.presentation.dialog.NewEditBudgetDialog
import kotlinx.coroutines.launch
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.available_in
import moinobudget.composeapp.generated.resources.create_budget_description
import moinobudget.composeapp.generated.resources.disposable_dd
import moinobudget.composeapp.generated.resources.due_in
import moinobudget.composeapp.generated.resources.edit_label_description
import moinobudget.composeapp.generated.resources.go_to_settings_help
import moinobudget.composeapp.generated.resources.my_incomes
import moinobudget.composeapp.generated.resources.new_budget
import moinobudget.composeapp.generated.resources.operation
import moinobudget.composeapp.generated.resources.payments_dd
import moinobudget.composeapp.generated.resources.to_put_aside_dd
import moinobudget.composeapp.generated.resources.upcoming_payments
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import presentation.BudgetBackground
import presentation.data.BudgetStyle
import presentation.data.BudgetUI
import presentation.data.ExpenseUI
import presentation.data.IncomeOrOutcome
import presentation.formatCurrency
import presentation.pager.pagerStateOpacity
import ui.Screen
import ui.theme.Orange80
import kotlin.math.absoluteValue

@Composable
fun DashboardScreen(
    preferences: AppPreferences,
    state: DashboardState,
    onEvent: (DashboardEvent) -> Unit,
    goTo: (Screen) -> Unit
) = Box {
    val scope = rememberCoroutineScope()
    var year by remember { mutableStateOf(false) }

    var addEditBudgetDialog by remember { mutableStateOf(false) }
    var budgetForDialog by remember { mutableStateOf<BudgetUI?>(null) }

    if (addEditBudgetDialog) NewEditBudgetDialog(
        budget = budgetForDialog,
        preferences = preferences,
        onDismiss = { addEditBudgetDialog = false
                    budgetForDialog = null },
        saveBudget = { onEvent(DashboardEvent.UpsertBudget(it)) },
        labels = state.labels,
    )

    val themePrimary = MaterialTheme.colorScheme.primary
    val themeOnPrimary = MaterialTheme.colorScheme.onPrimary
    val primary = remember { Animatable(themePrimary) }
    val onPrimary = remember { Animatable(themeOnPrimary) }

    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = primary.value,
            onPrimary = onPrimary.value
        )
    ) {
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
            Spacer(modifier = Modifier.height(8.dp))
            val budgetState = rememberPagerState(initialPage = 1, pageCount = { state.budgets.size + 1 })
            LaunchedEffect(key1 = budgetState.currentPage) {
                if (state.budgets.isNotEmpty()) {
                    val style = if (budgetState.currentPage != 0) state.budgets[budgetState.currentPage - 1].style
                        else BudgetStyle.CitrusJuice
                    primary.animateTo(if (preferences.theme.isDark) style.primary.second else style.primary.first)
                    onPrimary.animateTo(if (preferences.theme.isDark) style.onPrimary.second else style.onPrimary.first)
                }
            }
            LaunchedEffect(key1 = state.budgets) {
                if (budgetState.currentPage == 0 && budgetState.pageCount > 1) budgetState.scrollToPage(1)
            }
            HorizontalPager(
                modifier = Modifier.fillMaxWidth(),
                state = budgetState,
                contentPadding = PaddingValues(horizontal = 32.dp),
            ) { page ->
                if (page == 0) {
                    Box(modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(164.dp)
                        .aspectRatio(1.9f)
                        .clip(RoundedCornerShape(24.dp))
                        .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(24.dp))
                        .clickable(enabled = budgetState.currentPage == 0) { addEditBudgetDialog = true }
                        .pagerStateOpacity(budgetState, page),
                        contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(modifier = Modifier.size(80.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(16.dp),
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(Res.string.create_budget_description))
                            Text(stringResource(Res.string.create_budget_description).uppercase(),
                                modifier = Modifier.padding(top = 16.dp),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
                else {
                    val budget = state.budgets[page-1]
                    FinancialSummary(
                        modifier = Modifier.pagerStateOpacity(budgetState, page)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        preferences = preferences,
                        edit = { budgetForDialog = budget
                               addEditBudgetDialog = true},
                        year = year,
                        budget = budget)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            val dashboardState = rememberPagerState(pageCount = { state.budgets.size })
            DashboardTab(dashboardState, onSelect = { scope.launch {
                dashboardState.animateScrollToPage(it) }})
            Spacer(Modifier.height(8.dp))
            if (state.budgets.isNotEmpty()) HorizontalPager(state = dashboardState) { page ->
                Crossfade(targetState = budgetState.currentPage) { budgetPage ->
                    if (budgetPage > 0) {
                        val budget = state.budgets[budgetPage-1]
                        if (page == IncomeOrOutcome.Outcome.tabId) PaymentsSection(preferences = preferences,
                            budget = budget,
                            incomeOrOutcome = IncomeOrOutcome.Outcome,
                            amount = budget.upcomingPayments.first,
                            dueExpenses = budget.expenses)
                        else PaymentsSection(preferences = preferences,
                            budget = budget,
                            incomeOrOutcome = IncomeOrOutcome.Income,
                            amount = budget.rawIncomes.first,
                            dueExpenses = budget.expenses)
                    }
                }
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
fun QuickActions(
    createBudget: () -> Unit,
    addOperation: () -> Unit
) = Row(Modifier.padding(horizontal = 8.dp)) {
    QuickActionButton(
        modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
        title = stringResource(Res.string.new_budget),
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
    modifier: Modifier,
    edit: () -> Unit,
    year: Boolean,
    preferences: AppPreferences,
    budget: BudgetUI,
) = Card(modifier = modifier
    .height(164.dp)
    .aspectRatio(1.9f),
    shape = RoundedCornerShape(24.dp),
    elevation = CardDefaults.cardElevation(32.dp)
) {
    Box {
        BudgetBackground(modifier = Modifier.fillMaxSize(), background = budget.style.background)
        Column(Modifier.fillMaxSize().padding(8.dp)) {
            Box(modifier
                .fillMaxWidth()
                .offset(y = (-8).dp)) {
                Text(budget.title.uppercase(),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth().align(Alignment.Center))
                IconButton(onClick = edit,
                    modifier = Modifier.offset(x = -(24).dp).align(Alignment.CenterStart)) {
                    Icon(Icons.Default.Edit, contentDescription = stringResource(Res.string.edit_label_description))
                }
            }
            Column(Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
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
        }
        if (budget.labels.isNotEmpty()) LazyVerticalGrid(
            GridCells.Fixed(2),
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .width(68.dp)
                .padding(horizontal = 8.dp, vertical = 24.dp)
                .align(Alignment.CenterEnd)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))
                .padding(4.dp)
        ) {
            items(budget.labels) { label ->
                Box(Modifier.padding(2.dp)
                    .size(20.dp).aspectRatio(1f)
                    .clip(CircleShape).background(label.color))
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
    budget: BudgetUI,
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
        items(dueExpenses
            .filter { it.type == incomeOrOutcome && it.labels.any { label -> budget.labels.any { budget -> budget.id == label.id } } }
            .sortedBy { it.dueIn }) { dueExpense ->
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
                Box(Modifier.padding(horizontal = 4.dp).size(20.dp).clip(CircleShape).background(it.color))
            }
        }
    }
}