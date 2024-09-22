package feature.expenses.feature.add_edit_expense

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import data.repository.AppPreferences

@Composable
fun AddEditExpenseScreen(
    state: AddEditExpenseState,
    preferences: AppPreferences,
    goBack: () -> Unit
) = Surface(
    contentColor = MaterialTheme.colorScheme.onBackground,
    color = MaterialTheme.colorScheme.background
) {

}