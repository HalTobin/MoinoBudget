package data

import androidx.room.Room
import androidx.room.RoomDatabase
import data.db.ExpenseDatabase
import platform.Foundation.NSHomeDirectory

fun getExpenseDatabaseBuilder(): RoomDatabase.Builder<ExpenseDatabase> {
    val dbFile = NSHomeDirectory() + "/expenses.db"
    return Room.databaseBuilder<ExpenseDatabase>(
        name = dbFile,
        factory = { ExpenseDatabase::class.instantiateImpl() }
    )
}