package data.db.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import data.db.table.Budget
import data.db.table.BudgetLabelCrossRef
import data.db.table.Label

data class BudgetWithLabels(
    @Embedded val budget: Budget,
    @Relation(
        parentColumn = "budget_id",
        entityColumn = "label_id",
        associateBy = Junction(BudgetLabelCrossRef::class)
    )
    val labels: List<Label>
)