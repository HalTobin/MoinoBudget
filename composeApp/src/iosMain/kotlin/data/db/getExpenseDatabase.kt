package data.db

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import platform.Foundation.NSHomeDirectory

fun getExpenseDatabase(): ExpenseDatabase {
    val dbFile = NSHomeDirectory() + "/expenses.db"
    return Room.databaseBuilder<ExpenseDatabase>(
        name = dbFile,
        factory = { ExpenseDatabase::class.instantiateImpl() }
    )
        .setDriver(BundledSQLiteDriver())
        .build()
}