package data.db.table

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "budget_label_crossref",
    primaryKeys = ["budget_id", "label_id"],
)
data class BudgetLabelCrossRef(
    @ColumnInfo(name = "budget_id") val budgetId: Int,
    @ColumnInfo(name = "label_id") val labelId: Int
)