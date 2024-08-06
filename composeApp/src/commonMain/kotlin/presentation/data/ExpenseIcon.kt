package presentation.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Train
import androidx.compose.ui.graphics.vector.ImageVector

sealed class ExpenseIcon(val id: Int, val keywords: String, val icon: ImageVector) {
    data object Transport: ExpenseIcon(1, "transport", Icons.Default.Train)
}