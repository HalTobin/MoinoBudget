package presentation.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.Diversity3
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalGroceryStore
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.ui.graphics.vector.ImageVector

sealed class ExpenseIcon(val id: Int, val keywords: String, val icon: ImageVector) {
    data object Transport: ExpenseIcon(1, "transport", Icons.Default.Train)
    data object Housing: ExpenseIcon(2, "house", Icons.Default.Home)
    data object Grocery: ExpenseIcon(3, "groceries", Icons.Default.LocalGroceryStore)
    data object Phone: ExpenseIcon(4, "phone", Icons.Default.Phone)
    data object Help: ExpenseIcon(5, "diversity, solidarity", Icons.Default.Diversity3)
    data object Savings: ExpenseIcon(6, "savings", Icons.Default.Savings)
    data object Bank: ExpenseIcon(7, "bank", Icons.Default.AccountBalance)
    data object Incomes: ExpenseIcon(8, "incomes, cash", Icons.Default.Payments)
    data object Store: ExpenseIcon(9, "storefront, store, restaurant", Icons.Default.Storefront)
    data object Party: ExpenseIcon(10, "out, party", Icons.Default.Celebration)
    data object Film: ExpenseIcon(11, "film", Icons.Default.VideoLibrary)
    data object Internet: ExpenseIcon(12, "internet, wifi", Icons.Default.Wifi)
    data object Class: ExpenseIcon(12, "class", Icons.Default.Class)
    data object VideoGame: ExpenseIcon(13, "video game", Icons.Default.VideogameAsset)
    data object Health: ExpenseIcon(14, "health", Icons.Default.Medication)
    data object Water: ExpenseIcon(15, "water", Icons.Default.WaterDrop)
    data object Electricity: ExpenseIcon(16, "electricity", Icons.Default.Bolt)
}