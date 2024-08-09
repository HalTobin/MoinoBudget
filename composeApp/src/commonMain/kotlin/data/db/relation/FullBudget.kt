package data.db.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import data.db.table.Budget
import data.db.table.BudgetLabelCrossRef
import data.db.table.Expense
import data.db.table.ExpenseLabelCrossRef
import data.db.table.Label

data class FullBudget(
    @Embedded val budget: Budget,

    // Labels associated with the Budget via BudgetLabelCrossRef
    @Relation(
        parentColumn = "id", // Budget's ID
        entityColumn = "id", // Label's ID
        associateBy = Junction(
            value = BudgetLabelCrossRef::class,
            parentColumn = "budgetId",
            entityColumn = "labelId"
        )
    )
    val labels: List<Label>,

    // Expenses associated with Labels that are associated with the Budget
    @Relation(
        entity = Expense::class, // The final entity we want to get
        parentColumn = "id", // Budget's ID
        entityColumn = "id", // Expense's ID
        associateBy = Junction(
            value = ExpenseLabelCrossRef::class, // Junction table between Expense and Label
            parentColumn = "labelId", // Label ID in ExpenseLabelCrossRef
            entityColumn = "expenseId" // Expense ID in ExpenseLabelCrossRef
        )
    )
    val expenses: List<Expense>
)