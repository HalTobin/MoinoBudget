package data.db.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "savings")
data class Savings(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "savings_id") val id: Int = 0,
    val title: String,
    @ColumnInfo(defaultValue = "") val subtitle: String,
    val amount: Int,
    val goal: Int,
    val autoIncrement: Int,
    val lastMonthAutoIncrement: Long,
    val labelId: Int
)