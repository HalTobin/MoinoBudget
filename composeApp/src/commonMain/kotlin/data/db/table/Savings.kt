package data.db.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "savings")
data class Savings(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "savings_id") val id: Int = 0,
    val title: String,
    val subtitle: String,
    val iconId: Int?,
    val amount: Int,
    val type: Int, // 0 Accounts, 1 Savings, 2 Envelope, 3 Investments
    val goal: Int?,
    val autoIncrement: Int,
    val lastMonthAutoIncrement: Long,
    val color: Int?
)