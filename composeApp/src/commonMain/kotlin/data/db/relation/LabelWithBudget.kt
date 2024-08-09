package data.db.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import data.db.table.Budget
import data.db.table.BudgetLabelCrossRef
import data.db.table.Expense
import data.db.table.ExpenseLabelCrossRef
import data.db.table.Label

data class LabelWithBudget(
    @Embedded val budget: Budget,
    @Relation(
        parentColumn = "budgetId",
        entityColumn = "labelId",
        associateBy = Junction(BudgetLabelCrossRef::class)
    )
    val expenses: List<Expense>
)