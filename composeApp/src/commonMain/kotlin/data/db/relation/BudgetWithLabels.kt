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
        parentColumn = "labelId",
        entityColumn = "labelId",
        associateBy = Junction(BudgetLabelCrossRef::class)
    )
    val labels: List<Label>
)