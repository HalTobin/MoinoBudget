package presentation.data

data class BudgetUI(
    val id: Int,
    val title: String,
    val style: BudgetStyle,
    val labels: List<LabelUI>,
    val expenses: List<ExpenseUI>,
    // NOTE: Every Pair represent an <Monthly, Annual> value
    val rawIncomes: Pair<Float, Float> = Pair(0f, 0f),
    val upcomingPayments: Pair<Float, Float> = Pair(0f, 0f),
    val monthPayments: Pair<Float, Float> = Pair(0f, 0f),
    val toPutAside: Pair<Float, Float> = Pair(0f, 0f),
    val disposableIncomes: Pair<Float, Float> = Pair(0f, 0f)
)