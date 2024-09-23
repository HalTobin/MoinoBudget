package data.value

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.currency_afghani
import moinobudget.composeapp.generated.resources.currency_bitcoin
import moinobudget.composeapp.generated.resources.currency_cedi
import moinobudget.composeapp.generated.resources.currency_colon
import moinobudget.composeapp.generated.resources.currency_dol
import moinobudget.composeapp.generated.resources.currency_dong
import moinobudget.composeapp.generated.resources.currency_dram
import moinobudget.composeapp.generated.resources.currency_eur
import moinobudget.composeapp.generated.resources.currency_franc
import moinobudget.composeapp.generated.resources.currency_guarani
import moinobudget.composeapp.generated.resources.currency_hryvnia
import moinobudget.composeapp.generated.resources.currency_lari
import moinobudget.composeapp.generated.resources.currency_lira
import moinobudget.composeapp.generated.resources.currency_manat
import moinobudget.composeapp.generated.resources.currency_peso
import moinobudget.composeapp.generated.resources.currency_pound
import moinobudget.composeapp.generated.resources.currency_rial
import moinobudget.composeapp.generated.resources.currency_ruble
import moinobudget.composeapp.generated.resources.currency_rupee
import moinobudget.composeapp.generated.resources.currency_shekel
import moinobudget.composeapp.generated.resources.currency_som
import moinobudget.composeapp.generated.resources.currency_taka
import moinobudget.composeapp.generated.resources.currency_tenge
import moinobudget.composeapp.generated.resources.currency_togrog
import moinobudget.composeapp.generated.resources.currency_won
import moinobudget.composeapp.generated.resources.currency_yen_yuan
import moinobudget.composeapp.generated.resources.flag_de
import moinobudget.composeapp.generated.resources.flag_en
import moinobudget.composeapp.generated.resources.flag_es
import moinobudget.composeapp.generated.resources.flag_fr
import moinobudget.composeapp.generated.resources.flag_it
import moinobudget.composeapp.generated.resources.flag_ru
import moinobudget.composeapp.generated.resources.flag_ua
import moinobudget.composeapp.generated.resources.theme_dark
import moinobudget.composeapp.generated.resources.theme_dark_oled
import moinobudget.composeapp.generated.resources.theme_light
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

object PrefKey {
    const val CURRENCY = "currency"
    const val DECIMAL_MODE = "decimal_mode"
    const val THEME = "theme"
    const val LANGUAGE = "language"
    const val CARD_STYLE = "card_style"
}

object PrefDefault {
    const val CURRENCY = "€"

    // Values for themes
    const val THEME_DARK = "dark"
    const val THEME_DARK_OLED = "dark_oled"
    const val THEME_LIGHT = "light"
    const val THEME = THEME_DARK

    // Values for languages
    const val LANGUAGE_EN = "en"
    const val LANGUAGE_FR = "fr"
    const val LANGUAGE_ES = "es"
    const val LANGUAGE_IT = "eit"
    const val LANGUAGE_DE = "de"
    const val LANGUAGE_RU = "ru"
    const val LANGUAGE_UA = "ua"
    const val LANGUAGE = LANGUAGE_EN
}

enum class Theme(val color: Color, val key: String, val title: StringResource, val isDark: Boolean) {
    Dark(color = Color.DarkGray, key = PrefDefault.THEME_DARK, title = Res.string.theme_dark, isDark = true),
    DarkOled(color = Color.Black, key = PrefDefault.THEME_DARK_OLED, title = Res.string.theme_dark_oled, isDark = true),
    Light(color = Color.White, key = PrefDefault.THEME_LIGHT, title = Res.string.theme_light, isDark = false);
}

enum class Language(val key: String, val title: String, val flag: DrawableResource) {
    English(key = PrefDefault.LANGUAGE_EN, title = "English", flag = Res.drawable.flag_en),
    French(key = PrefDefault.LANGUAGE_FR, title = "Français", flag = Res.drawable.flag_fr),
    Spanish(key = PrefDefault.LANGUAGE_ES, title = "Español", flag = Res.drawable.flag_es),
    Italian(key = PrefDefault.LANGUAGE_IT, title = "Italiano", flag = Res.drawable.flag_it),
    German(key = PrefDefault.LANGUAGE_DE, title = "Deutsch", flag = Res.drawable.flag_de),
    Russian(key = PrefDefault.LANGUAGE_RU, title = "Русский", flag = Res.drawable.flag_ru),
    Ukrainian(key = PrefDefault.LANGUAGE_UA, title = "Українська", flag = Res.drawable.flag_ua),
}

enum class Currency(val key: String, val title: StringResource, val sign: String, val position: CurrencyPosition = CurrencyPosition.End, val decimalMode: Boolean) {
    Euro(key = "eur", title = Res.string.currency_eur, sign = "€", decimalMode = true),
    Dollar(key = "dol", title = Res.string.currency_dol,sign = "$", position = CurrencyPosition.Start, decimalMode = true),
    Ruble(key = "ruble", title = Res.string.currency_ruble, sign = "₽", decimalMode = true),
    YenYuan(key = "yen_yuan", title = Res.string.currency_yen_yuan, sign = "¥", decimalMode = true),
    Pound(key = "pound", title = Res.string.currency_pound, sign = "£", decimalMode = true),
    Lira(key = "lira", title = Res.string.currency_lira, sign = "₺", decimalMode = true),
    Rupee(key = "rupee", title = Res.string.currency_rupee, sign = "₹", decimalMode = true),
    Franc(key = "franc", title = Res.string.currency_franc, sign = "₣", decimalMode = true),
    Peso(key = "peso", title = Res.string.currency_peso, sign = "₱", decimalMode = true),
    Bitcoin(key = "btc", title = Res.string.currency_bitcoin, sign = "₿", decimalMode = true),
    Hryvnia(key = "hryvnia", title = Res.string.currency_hryvnia, sign = "₴", decimalMode = true),
    Won(key = "won", title = Res.string.currency_won, sign = "₩", decimalMode = true),
    Lari(key = "lari", title = Res.string.currency_lari, sign = "₾", decimalMode = true),
    Cedi(key = "cedi", title = Res.string.currency_cedi, sign = "₵", decimalMode = true),
    Togrog(key = "tögrög", title = Res.string.currency_togrog, sign = "₮", decimalMode = true),
    Tenge(key = "tenge", title = Res.string.currency_tenge, sign = "₸", decimalMode = true),
    Taka(key = "taka", title = Res.string.currency_taka, sign = "৳", decimalMode = true),
    Som(key = "som", title = Res.string.currency_som, sign = "\u20C0", decimalMode = true),
    Shekel(key = "shekel", title = Res.string.currency_shekel, sign = "₪", decimalMode = true),
    SriLankaRupee(key = "sri_rupee", title = Res.string.currency_rupee, sign = "රු", decimalMode = true),
    Rial(key = "rial", title = Res.string.currency_rial, sign = "﷼", decimalMode = true),
    Dram(key = "dram", title = Res.string.currency_dram, sign = "֏", decimalMode = true),
    Colon(key = "colon", title = Res.string.currency_colon, sign = "₡", decimalMode = true),
    Afghani(key = "afghani", title = Res.string.currency_afghani, sign = "؋", decimalMode = true),
    Manat(key = "manat", title = Res.string.currency_manat, sign = "₼", decimalMode = true),
    Guarani(key = "guarani", title = Res.string.currency_guarani, sign = "₲", decimalMode = true),
    Dong(key = "dong", title = Res.string.currency_dong, sign = "₫", decimalMode = true),
    ;
}

enum class CurrencyPosition { Start, End }