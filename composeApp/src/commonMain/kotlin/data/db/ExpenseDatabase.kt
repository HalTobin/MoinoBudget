package data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import data.db.dao.ExpenseDao
import data.db.dao.ExpenseLabelDao
import data.db.dao.LabelDao
import data.db.table.Label
import data.db.table.Expense

@Database(
    entities = [Expense::class, Label::class],
    version = 1
)
abstract class ExpenseDatabase: RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao
    abstract fun labelDao(): LabelDao
    abstract fun expenseLabelDao(): ExpenseLabelDao

}