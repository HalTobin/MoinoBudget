package data.db.table

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["budgetId", "labelId"],
    foreignKeys = [
        ForeignKey(
            entity = Budget::class,
            parentColumns = ["id"],
            childColumns = ["budgetId"],
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
data class BudgetLabelCrossRef(
    val budgetId: Int,
    val labelId: Int
)