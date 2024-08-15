package data.db.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class Budget(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "budget_id") val id: Int = 0,
    @ColumnInfo(name = "order_index") val orderIndex: Int = 0,
    val title: String,
    val bgId: Int
)