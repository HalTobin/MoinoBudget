package presentation.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Green
import data.repository.AppPreferences
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.bg_card_1
import moinobudget.composeapp.generated.resources.style_citrus
import moinobudget.composeapp.generated.resources.style_grass_sea
import moinobudget.composeapp.generated.resources.style_red_waves
import moinobudget.composeapp.generated.resources.style_winter
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import presentation.data.BudgetStyleColors.Cyan
import presentation.data.BudgetStyleColors.LightBlue
import presentation.data.BudgetStyleColors.Purple
import presentation.data.BudgetStyleColors.RedPink
import presentation.data.BudgetStyleColors.Yellow
import ui.theme.Cyan40
import ui.theme.Cyan80
import ui.theme.CyanGrey40
import ui.theme.CyanGrey80
import ui.theme.Green40
import ui.theme.Green80
import ui.theme.GreenGrey40
import ui.theme.GreenGrey80
import ui.theme.Orange40
import ui.theme.Orange80
import ui.theme.OrangeGrey40
import ui.theme.OrangeGrey80
import ui.theme.Red40
import ui.theme.Red80
import ui.theme.RedGrey40
import ui.theme.RedGrey80

enum class BudgetStyle(
    val id: Int,
    val title: StringResource,
    val background: Background,
    val primary: Pair<Color, Color> = Pair(Orange40, Orange80), // <Light, Dark>
    val onPrimary: Pair<Color, Color> = Pair(OrangeGrey40, OrangeGrey80)
) {
    RedWaves(id = 1, title = Res.string.style_red_waves, Background.Image(Res.drawable.bg_card_1),
        primary = Pair(Red40, Red80), onPrimary = Pair(RedGrey40, RedGrey80)),
    Winter(id = 2, title = Res.string.style_winter, Background.Gradient(listOf(Cyan, LightBlue, Purple)),
        primary = Pair(Cyan40, Cyan80), onPrimary = Pair(CyanGrey40, CyanGrey80)),
    CitrusJuice(id = 3, title = Res.string.style_citrus, Background.Gradient(listOf(Yellow, RedPink)),),
    GrassAndSea(id = 4, title = Res.string.style_grass_sea, Background.Gradient(listOf(Blue, Green)),
        primary = Pair(Green40, Green80), onPrimary = Pair(GreenGrey40, GreenGrey80));

    fun getPrimary(preferences: AppPreferences): Color =
        if (preferences.theme.isDark) primary.second else primary.first

    fun getOnPrimary(preferences: AppPreferences): Color =
        if (preferences.theme.isDark) onPrimary.second else onPrimary.first

    companion object {
        val list = listOf(RedWaves, Winter, CitrusJuice, GrassAndSea)
    }
}

sealed class Background {
    data class Image(val image: DrawableResource): Background()
    data class Gradient(val colors: List<Color>): Background()
}

object BudgetStyleColors {
    val Cyan = Color(0xFF00FFFF)
    val LightBlue = Color(0xFF6666AA)
    val Purple = Color(0xFFFF00FF)

    val Yellow = Color(0xFFFDD835)
    val RedPink = Color(0xFFFA2A55)
    //val Orange = Color(0xFFFFEB3B)
}