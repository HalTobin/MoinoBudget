package data.db.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val iconId: Int,
    val frequency: Int, // 0: Monthly, 1: Quarterly, 2: Biannually, 3: Annually
    val monthOffset: Int?, // Only if annual
    val day: Int,
    val payed: Boolean = false
)