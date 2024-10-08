package presentation.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.Diversity3
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalGroceryStore
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SmokingRooms
import androidx.compose.material.icons.filled.SsidChart
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.VapingRooms
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.ui.graphics.vector.ImageVector

enum class ExpenseIcon(val id: Int, val keywords: String, val icon: ImageVector) {
    DefaultIncome(1, "income", Icons.AutoMirrored.Filled.TrendingUp),
    DefaultOutcome(2, "outcome", Icons.AutoMirrored.Filled.TrendingDown),
    Transport(3, "transport", Icons.Default.Train),
    Housing(4, "house", Icons.Default.Home),
    Grocery(5, "groceries", Icons.Default.LocalGroceryStore),
    Phone(6, "phone", Icons.Default.Phone),
    Help(7, "diversity, solidarity", Icons.Default.Diversity3),
    Savings(8, "savings", Icons.Default.Savings),
    Bank(9, "bank", Icons.Default.AccountBalance),
    Incomes(10, "incomes, cash", Icons.Default.Payments),
    Store(11, "storefront, store, restaurant", Icons.Default.Storefront),
    Party(12, "out, party", Icons.Default.Celebration),
    Film(13, "film", Icons.Default.VideoLibrary),
    Internet(14, "internet, wifi", Icons.Default.Wifi),
    Class(15, "class", Icons.Default.Class),
    VideoGame(16, "video game", Icons.Default.VideogameAsset),
    Health(17, "health", Icons.Default.Medication),
    Water(18, "water", Icons.Default.WaterDrop),
    Electricity(19, "electricity", Icons.Default.Bolt),
    Cloud(20, "cloud", Icons.Default.Cloud),
    School(21, "school, classes, teacher", Icons.Default.School),
    Smoke(22, "smoke, vape, cigarettes", Icons.Default.SmokingRooms),
    Vape(23, "smoke, vape, liquids, cigarettes", Icons.Default.VapingRooms),
    Plane(24, "transport, plane", Icons.Default.Flight),
    Music(25, "music, art", Icons.Default.MusicNote),
    Taxes(26, "taxes", Icons.Default.Receipt),
    Crypto(27, "crypto, crypto currency, bitcoin", Icons.Default.CurrencyBitcoin),
    Apartment(28, "housing, apartment", Icons.Default.Apartment),
    Investment(29, "investment", Icons.Default.SsidChart),
    Restaurant(30, "restaurant", Icons.Default.Restaurant);

    companion object {
        val list = ExpenseIcon.entries.map { it }.sortedBy { it.id }

        fun findById(id: Int): ExpenseIcon = list.find { it.id == id } ?: Transport
    }
}