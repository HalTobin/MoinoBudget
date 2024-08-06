package data.value

import androidx.compose.ui.graphics.Color
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.flag_en
import moinobudget.composeapp.generated.resources.flag_es
import moinobudget.composeapp.generated.resources.flag_fr
import moinobudget.composeapp.generated.resources.flag_ru
import moinobudget.composeapp.generated.resources.theme_dark
import moinobudget.composeapp.generated.resources.theme_dark_oled
import moinobudget.composeapp.generated.resources.theme_light
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

object PrefKey {
    const val CURRENCY = "currency"
    const val DECIMAL_MODE = "decimal_mode"
    const val THEME = "theme"
    const val LANGUAGE = "language"
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
    const val LANGUAGE_RU = "ru"
    const val LANGUAGE_ES = "es"
    const val LANGUAGE = LANGUAGE_EN
}

enum class Theme(val color: Color, val key: String, val title: StringResource) {
    Dark(color = Color.DarkGray, key = PrefDefault.THEME_DARK, title = Res.string.theme_dark),
    DarkOled(color = Color.Black, key = PrefDefault.THEME_DARK_OLED, title = Res.string.theme_dark_oled),
    Light(color = Color.White, key = PrefDefault.THEME_LIGHT, title = Res.string.theme_light);
    companion object {
        val list = listOf(Light, Dark, DarkOled)
    }
}

enum class Language(val key: String, val title: String, val flag: DrawableResource) {
    English(key = PrefDefault.LANGUAGE_EN, title = "English", flag = Res.drawable.flag_en),
    French(key = PrefDefault.LANGUAGE_FR, title = "Français", flag = Res.drawable.flag_fr),
    Spanish(key = PrefDefault.LANGUAGE_ES, title = "Español", flag = Res.drawable.flag_es),
    Russian(key = PrefDefault.LANGUAGE_RU, title = "Русский", flag = Res.drawable.flag_ru);
    companion object {
        val list = listOf(English, French, Spanish, Russian)
    }
}

enum class Currency(val key: String, val sign: String, val decimalMode: Boolean) {
    Euro(key = "eur", sign = "€", decimalMode = true),
    Dollar(key = "usd", sign = "$", decimalMode = true),
    Ruble(key = "rub", sign = "₽", decimalMode = true),
    YenYuan(key = "yen/yuan", sign = "¥", decimalMode = true),
    Pound(key = "pound", sign = "£", decimalMode = true),
    Lira(key = "pound", sign = "₺", decimalMode = true);
    companion object {
        val list = listOf(Euro, Dollar, Ruble, YenYuan, Pound, Lira)
    }
}