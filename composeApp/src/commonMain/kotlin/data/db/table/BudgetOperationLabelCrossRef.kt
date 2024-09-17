package data.db.table

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "operation_label_crossref",
    primaryKeys = ["operation_id", "label_id"]
)
data class BudgetOperationLabelCrossRef(
    @ColumnInfo(name = "operation_id") val operationId: Int,
    @ColumnInfo(name = "label_id")  val labelId: Int
)