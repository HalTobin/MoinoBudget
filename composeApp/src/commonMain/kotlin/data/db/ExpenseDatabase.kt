package data.db

import androidx.room.AutoMigration
import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import data.db.dao.BudgetDao
import data.db.dao.BudgetLabelDao
import data.db.dao.ExpenseDao
import data.db.dao.ExpenseLabelDao
import data.db.dao.LabelDao
import data.db.dao.SavingsDao
import data.db.table.Budget
import data.db.table.BudgetLabelCrossRef
import data.db.table.Expense
import data.db.table.ExpenseLabelCrossRef
import data.db.table.Label
import data.db.table.Savings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(
    entities = [Expense::class,
        Label::class,
        ExpenseLabelCrossRef::class,
        Budget::class,
        BudgetLabelCrossRef::class,
        Savings::class
    ],
    autoMigrations = [
        AutoMigration (
            from = 1,
            to = 2,
        ),
        AutoMigration (
            from = 2,
            to = 3,
        ),
        AutoMigration (
            from = 3,
            to = 4,
        )
    ],
    version = 4
)
@ConstructedBy(ExpenseDbCtor::class) // NEW
abstract class ExpenseDatabase: RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao
    abstract fun labelDao(): LabelDao
    abstract fun expenseLabelDao(): ExpenseLabelDao
    abstract fun budgetDao(): BudgetDao
    abstract fun budgetLabelDao(): BudgetLabelDao
    abstract fun savingsDao(): SavingsDao

}

fun getExpenseDatabase(
    builder: RoomDatabase.Builder<ExpenseDatabase>
): ExpenseDatabase = builder.setDriver(BundledSQLiteDriver())
    .setQueryCoroutineContext(Dispatchers.IO)
    .build()

expect object ExpenseDbCtor : RoomDatabaseConstructor<ExpenseDatabase>