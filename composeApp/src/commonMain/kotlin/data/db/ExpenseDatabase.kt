package data.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import data.db.dao.BudgetDao
import data.db.dao.BudgetLabelDao
import data.db.dao.BudgetOperationDao
import data.db.dao.BudgetOperationLabelDao
import data.db.dao.EnvelopeDao
import data.db.dao.ExpenseDao
import data.db.dao.LabelDao
import data.db.dao.SavingsDao
import data.db.table.Budget
import data.db.table.BudgetLabelCrossRef
import data.db.table.BudgetOperation
import data.db.table.BudgetOperationLabelCrossRef
import data.db.table.Envelope
import data.db.table.Expense
import data.db.table.Label
import data.db.table.Savings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(
    entities = [BudgetOperation::class,
        Label::class,
        BudgetOperationLabelCrossRef::class,
        Budget::class,
        BudgetLabelCrossRef::class,
        Savings::class,
        Envelope::class,
        Expense::class
    ],
    version = 14
)
@ConstructedBy(ExpenseDbCtor::class)
abstract class ExpenseDatabase: RoomDatabase() {

    abstract fun budgetOperation(): BudgetOperationDao
    abstract fun labelDao(): LabelDao
    abstract fun budgetOperationLabelDao(): BudgetOperationLabelDao
    abstract fun budgetDao(): BudgetDao
    abstract fun budgetLabelDao(): BudgetLabelDao
    abstract fun savingsDao(): SavingsDao
    abstract fun envelopeDao(): EnvelopeDao
    abstract fun expenseDao(): ExpenseDao

}

fun getExpenseDatabase(
    builder: RoomDatabase.Builder<ExpenseDatabase>
): ExpenseDatabase = builder.setDriver(BundledSQLiteDriver())
    .setQueryCoroutineContext(Dispatchers.IO)
    .build()

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object ExpenseDbCtor : RoomDatabaseConstructor<ExpenseDatabase> {
    override fun initialize(): ExpenseDatabase
}