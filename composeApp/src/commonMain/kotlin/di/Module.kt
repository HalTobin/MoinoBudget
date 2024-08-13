package di

import data.db.ExpenseDatabase
import data.db.dao.LabelDao
import data.repository.LabelRepository
import data.repository.LabelRepositoryImpl
import feature.dashboard.presentation.DashboardViewModel
import feature.settings.SettingsViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ui.MainViewModel

expect object Module {
    val modules: Module
}

object ModuleVM {
    val viewModels = module {
        viewModelOf(::MainViewModel)
        viewModelOf(::DashboardViewModel)
        viewModelOf(::SettingsViewModel)
    }
}

object ModuleRepositories {
    val repositories = module {
        single { provideLabelRepository(get()) }.bind<LabelRepository>()
    }

    private fun provideLabelRepository(labelDao: LabelDao) = LabelRepositoryImpl(labelDao)
}

object ModuleDAO {
    val dao = module {
        single { provideLabelDao(get()) }
        single { provideExpenseDao(get()) }
        single { provideExpenseLabelDao(get()) }
        single { provideBudgetDao(get()) }
        single { provideBudgetLabelDao(get()) }
    }

    private fun provideLabelDao(db: ExpenseDatabase) = db.labelDao()
    private fun provideExpenseDao(db: ExpenseDatabase) = db.expenseDao()
    private fun provideExpenseLabelDao(db: ExpenseDatabase) = db.expenseLabelDao()
    private fun provideBudgetDao(db: ExpenseDatabase) = db.budgetDao()
    private fun provideBudgetLabelDao(db: ExpenseDatabase) = db.budgetLabelDao()
}