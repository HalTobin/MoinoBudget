package presentation.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.Cloud
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

enum class ExpenseIcon(val id: Int, val keywords: String, val icon: ImageVector) {
    Transport(1, "transport", Icons.Default.Train),
    Housing(2, "house", Icons.Default.Home),
    Grocery(3, "groceries", Icons.Default.LocalGroceryStore),
    Phone(4, "phone", Icons.Default.Phone),
    Help(5, "diversity, solidarity", Icons.Default.Diversity3),
    Savings(6, "savings", Icons.Default.Savings),
    Bank(7, "bank", Icons.Default.AccountBalance),
    Incomes(8, "incomes, cash", Icons.Default.Payments),
    Store(9, "storefront, store, restaurant", Icons.Default.Storefront),
    Party(10, "out, party", Icons.Default.Celebration),
    Film(11, "film", Icons.Default.VideoLibrary),
    Internet(12, "internet, wifi", Icons.Default.Wifi),
    Class(12, "class", Icons.Default.Class),
    VideoGame(13, "video game", Icons.Default.VideogameAsset),
    Health(14, "health", Icons.Default.Medication),
    Water(15, "water", Icons.Default.WaterDrop),
    Electricity(16, "electricity", Icons.Default.Bolt),
    Cloud(17, "cloud", Icons.Default.Cloud);

    companion object {
        val list = listOf(Transport, Housing, Grocery, Phone, Help, Savings, Bank, Incomes,
            Store, Party, Film, Internet, Class, VideoGame, Health, Electricity, Cloud)

        fun findById(id: Int): ExpenseIcon = list.find { it.id == id } ?: Transport
    }
}