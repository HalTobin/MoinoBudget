package data.db

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider

actual fun getInMemoryDataBase(): RoomDatabase.Builder<ExpenseDatabase> =
    Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        ExpenseDatabase::class.java
    )