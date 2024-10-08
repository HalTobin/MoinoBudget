package data.db.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget_operations")
data class BudgetOperation(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "operation_id") val id: Int = 0,
    val title: String,
    val amount: Float,
    val iconId: Int,
    val isIncome: Boolean,
    val frequency: Int, // 0: Monthly, 1: Quarterly, 2: Biannually, 3: Annually
    val monthOffset: Int?, // Only if not monthly
    val day: Int,
)