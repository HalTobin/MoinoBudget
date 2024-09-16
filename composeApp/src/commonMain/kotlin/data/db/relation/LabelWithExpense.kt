package data.db.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import data.db.table.BudgetOperation
import data.db.table.BudgetOperationLabelCrossRef
import data.db.table.Label

data class LabelWithExpenses(
    @Embedded val label: Label,
    @Relation(
        parentColumn = "label_id",
        entityColumn = "expense_id",
        associateBy = Junction(BudgetOperationLabelCrossRef::class)
    )
    val expens: List<BudgetOperation>
)