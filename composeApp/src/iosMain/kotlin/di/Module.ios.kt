package di

import data.createDatastore
import data.db.ExpenseDatabase
import data.db.getExpenseDatabase
import data.getExpenseDatabaseBuilder
import data.repository.PreferenceRepository
import data.repository.PreferenceRepositoryImpl
import org.koin.dsl.bind
import org.koin.dsl.module

actual object Module {
    actual val modules = module {
        single { PreferenceRepositoryImpl(createDatastore()) }.bind<PreferenceRepository>()
        single { getExpenseDatabase(getExpenseDatabaseBuilder()) }.bind<ExpenseDatabase>()
    }
}