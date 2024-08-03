package di

import data.db.ExpenseDatabase
import data.db.getExpenseDatabase
import org.koin.dsl.bind
import org.koin.dsl.module

actual object ModuleDb {
    actual val module = module {
        single { getExpenseDatabase() }.bind<ExpenseDatabase>()
    }
}