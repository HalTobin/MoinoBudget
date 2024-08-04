package data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import data.db.ExpenseDatabase

fun getExpenseDatabaseBuilder(context: Context): RoomDatabase.Builder<ExpenseDatabase> {
    val dbFile = context.getDatabasePath("expenses.db")
    return Room.databaseBuilder<ExpenseDatabase>(context = context, name = dbFile.absolutePath)
}