package data.db.table

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["expenseId", "labelId"],
    foreignKeys = [
        ForeignKey(
            entity = Expense::class,
            parentColumns = ["id"],
            childColumns = ["expenseId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Label::class,
            parentColumns = ["id"],
            childColumns = ["labelId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ExpenseLabelCrossRef(
    val expenseId: Int,
    val labelId: Int
)