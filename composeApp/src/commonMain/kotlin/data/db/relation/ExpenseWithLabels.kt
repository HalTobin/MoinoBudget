package data.db.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import data.db.table.BudgetOperation
import data.db.table.BudgetOperationLabelCrossRef
import data.db.table.Label

data class ExpenseWithLabels(
    @Embedded val budgetOperation: BudgetOperation,
    @Relation(
        parentColumn = "expense_id",
        entityColumn = "label_id",
        associateBy = Junction(BudgetOperationLabelCrossRef::class)
    )
    val labels: List<Label>
)