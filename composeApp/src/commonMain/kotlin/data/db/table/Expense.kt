package data.db.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val amount: Float,
    val iconId: Int,
    val frequency: Int, // 0: Monthly, 1: Quarterly, 2: Biannually, 3: Annually
    val monthOffset: Int?, // Only if not monthly
    val day: Int,
    val lastPayment: Long
)