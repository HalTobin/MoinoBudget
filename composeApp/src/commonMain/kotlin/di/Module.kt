package di

import data.db.ExpenseDatabase
import data.db.dao.BudgetDao
import data.db.dao.BudgetLabelDao
import data.db.dao.ExpenseDao
import data.db.dao.ExpenseLabelDao
import data.db.dao.LabelDao
import data.repository.BudgetRepository
import data.repository.BudgetRepositoryImpl
import data.repository.ExpenseRepository
import data.repository.ExpenseRepositoryImpl
import data.repository.LabelRepository
import data.repository.LabelRepositoryImpl
import feature.add_edit_expense.presentation.AddEditExpenseViewModel
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
        viewModelOf(::AddEditExpenseViewModel)
        viewModelOf(::SettingsViewModel)
    }
}

object ModuleRepositories {
    val repositories = module {
        single { provideLabelRepository(get()) }.bind<LabelRepository>()
        single { provideBudgetRepository(get(), get(), get()) }.bind<BudgetRepository>()
        single { provideExpenseRepository(get(), get()) }.bind<ExpenseRepository>()
    }

    private fun provideLabelRepository(labelDao: LabelDao) = LabelRepositoryImpl(labelDao)
    private fun provideBudgetRepository(
        budgetDao: BudgetDao,
        budgetLabelDao: BudgetLabelDao,
        expenseLabelDao: ExpenseLabelDao
    ): BudgetRepository = BudgetRepositoryImpl(budgetDao, budgetLabelDao, expenseLabelDao)
    private fun provideExpenseRepository(
        expenseDao: ExpenseDao,
        expenseLabelDao: ExpenseLabelDao
    ): ExpenseRepository = ExpenseRepositoryImpl(expenseDao, expenseLabelDao)
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