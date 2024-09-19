package data.db.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "expense_id") val id: Int,
    val title: String,
    @ColumnInfo(name = "envelope_id") val envelopeId: Int,
    val amount: Float,
    val iconId: Int?,
    val timestamp: Long
)
