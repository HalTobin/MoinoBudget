package data.db.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import data.db.table.Expense
import data.db.table.ExpenseLabelCrossRef
import data.db.table.Label

data class LabelWithExpenses(
    @Embedded val label: Label,
    @Relation(
        parentColumn = "labelId",
        entityColumn = "expenseId",
        associateBy = Junction(ExpenseLabelCrossRef::class)
    )
    val expenses: List<Expense>
)