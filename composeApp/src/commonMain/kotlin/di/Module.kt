package di

import data.db.ExpenseDatabase
import data.db.dao.BudgetDao
import data.db.dao.BudgetLabelDao
import data.db.dao.BudgetOperationDao
import data.db.dao.BudgetOperationLabelDao
import data.db.dao.EnvelopeDao
import data.db.dao.LabelDao
import data.db.dao.SavingsDao
import data.repository.BudgetRepository
import data.repository.BudgetRepositoryImpl
import data.repository.EnvelopeRepository
import data.repository.EnvelopeRepositoryImpl
import data.repository.ExpenseRepository
import data.repository.ExpenseRepositoryImpl
import data.repository.LabelRepository
import data.repository.LabelRepositoryImpl
import data.repository.SavingsRepository
import data.repository.SavingsRepositoryImpl
import feature.budgets.feature.add_edit_budgets.presentation.AddEditExpenseViewModel
import feature.budgets.feature.budgets_list.presentation.DashboardViewModel
import feature.expenses.feature.envelope_list.presentation.EnvelopeViewModel
import feature.savings.feature.add_edit_savings.presentation.AddEditSavingsViewModel
import feature.savings.feature.savings_detail.SavingsDetailsViewModel
import feature.savings.feature.savings_list.presentation.SavingsViewModel
import feature.settings.SettingsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
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
        viewModelOf(::SavingsViewModel)
        viewModelOf(::AddEditSavingsViewModel)
        viewModelOf(::SavingsDetailsViewModel)
        viewModelOf(::EnvelopeViewModel)
    }
}

object ModuleRepositories {
    val repositories = module {
        single { provideLabelRepository(get()) }.bind<LabelRepository>()
        single { provideBudgetRepository(get(), get(), get()) }.bind<BudgetRepository>()
        single { provideExpenseRepository(get(), get()) }.bind<ExpenseRepository>()
        single { provideSavingsRepository(get()) }.bind<SavingsRepository>()
        single { provideEnvelopeRepository(get()) }.bind<EnvelopeRepository>()
    }

    private fun provideLabelRepository(labelDao: LabelDao) = LabelRepositoryImpl(labelDao)

    private fun provideBudgetRepository(
        budgetDao: BudgetDao,
        budgetLabelDao: BudgetLabelDao,
        budgetOperationLabelDao: BudgetOperationLabelDao
    ): BudgetRepository = BudgetRepositoryImpl(budgetDao, budgetLabelDao, budgetOperationLabelDao)

    private fun provideExpenseRepository(
        budgetOperationDao: BudgetOperationDao,
        budgetOperationLabelDao: BudgetOperationLabelDao
    ): ExpenseRepository = ExpenseRepositoryImpl(budgetOperationDao, budgetOperationLabelDao)

    private fun provideSavingsRepository(savingsDao: SavingsDao): SavingsRepository = SavingsRepositoryImpl(savingsDao)

    private fun provideEnvelopeRepository(envelopeDao: EnvelopeDao): EnvelopeRepository = EnvelopeRepositoryImpl(envelopeDao)
}

object ModuleDAO {
    val dao = module {
        single { provideLabelDao(get()) }
        single { provideExpenseDao(get()) }
        single { provideExpenseLabelDao(get()) }
        single { provideBudgetDao(get()) }
        single { provideBudgetLabelDao(get()) }
        single { provideSavingsDao(get()) }
        single { provideEnvelopeDao(get()) }
    }

    private fun provideLabelDao(db: ExpenseDatabase) = db.labelDao()
    private fun provideExpenseDao(db: ExpenseDatabase) = db.expenseDao()
    private fun provideExpenseLabelDao(db: ExpenseDatabase) = db.expenseLabelDao()
    private fun provideBudgetDao(db: ExpenseDatabase) = db.budgetDao()
    private fun provideBudgetLabelDao(db: ExpenseDatabase) = db.budgetLabelDao()
    private fun provideSavingsDao(db: ExpenseDatabase) = db.savingsDao()
    private fun provideEnvelopeDao(db: ExpenseDatabase) = db.envelopeDao()
}