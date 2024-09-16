package feature.budgets.feature.budgets_list.data

fun MonthYearPair(monthly: Float, annual: Float): Pair<Float, Float> = Pair(monthly, annual)
fun MonthYearPair(annual: Float) = Pair(annual / 12f, annual)
fun MonthYearPair(annual: Double) = Pair(annual.toFloat() / 12f, annual.toFloat())

val Pair<Float, Float>.monthly: Float get() = this.first
val Pair<Float, Float>.annual: Float get() = this.second