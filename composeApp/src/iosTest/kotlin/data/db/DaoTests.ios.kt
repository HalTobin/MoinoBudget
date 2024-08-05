package data.db

import androidx.room.Room
import androidx.room.RoomDatabase

actual fun getInMemoryDataBase(): RoomDatabase.Builder<ExpenseDatabase> =
    Room.inMemoryDatabaseBuilder { ExpenseDatabase::class.instantiateImpl() }