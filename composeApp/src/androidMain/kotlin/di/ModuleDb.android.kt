package di

import data.db.ExpenseDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.bind
import org.koin.dsl.module
import org.moineaufactory.moinobudget.data.db.getExpenseDatabase

actual object ModuleDb {
    actual val module = module {
        single { getExpenseDatabase(androidApplication()) }.bind<ExpenseDatabase>()
    }
}