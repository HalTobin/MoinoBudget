package data.db.table

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "expense_label_crossref",
    primaryKeys = ["expense_id", "label_id"]
)
data class ExpenseLabelCrossRef(
    @ColumnInfo(name = "expense_id") val expenseId: Int,
    @ColumnInfo(name = "label_id")  val labelId: Int
)