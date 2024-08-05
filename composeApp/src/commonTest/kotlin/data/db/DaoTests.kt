package data.db

import androidx.room.RoomDatabase
import data.db.dao.ExpenseDao
import data.db.dao.ExpenseLabelDao
import data.db.dao.LabelDao
import data.db.table.Label
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.runBlocking
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DaoTests {

    private lateinit var db: ExpenseDatabase
    private lateinit var expenseDao: ExpenseDao
    private lateinit var labelDao: LabelDao
    private lateinit var expenseLabelDao: ExpenseLabelDao

    @BeforeTest
    fun setup() {
        db = getRoomDatabase(builder = getInMemoryDataBase())
        expenseDao = db.expenseDao()
        labelDao = db.labelDao()
        expenseLabelDao = db.expenseLabelDao()
    }

    @Test
    fun `test insert label`() = runBlocking(Dispatchers.IO) {
        val labelId = 1
        val labelTest = Label(id = labelId, "Test Label", 0xFFFFFF)

        labelDao.upsert(labelTest)
        val labelDb = labelDao.getById(labelId)

        assertEquals(labelTest, labelDb)
    }

    @AfterTest
    fun clear() = db.close()

}

fun getRoomDatabase(
    builder: RoomDatabase.Builder<ExpenseDatabase>,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): ExpenseDatabase {
    return builder
        .setQueryCoroutineContext(dispatcher)
        .build()
}

expect fun getInMemoryDataBase(): RoomDatabase.Builder<ExpenseDatabase>