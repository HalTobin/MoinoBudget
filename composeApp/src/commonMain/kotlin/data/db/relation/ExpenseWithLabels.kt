package data.db.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import data.db.table.Expense
import data.db.table.ExpenseLabelCrossRef
import data.db.table.Label

data class ExpenseWithLabels(
    @Embedded val expense: Expense,
    @Relation(
        parentColumn = "expense_id",
        entityColumn = "label_id",
        associateBy = Junction(ExpenseLabelCrossRef::class)
    )
    val labels: List<Label>
)