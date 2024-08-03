package data

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import data.db.ExpenseDatabase

fun getExpenseDatabase(context: Context): ExpenseDatabase {
    val dbFile = context.getDatabasePath("expenses.db")
    return Room.databaseBuilder<ExpenseDatabase>(context = context, name = dbFile.absolutePath)
        .setDriver(BundledSQLiteDriver())
        .build()
}