package presentation.data

import androidx.compose.ui.graphics.Color
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.bg_card_1
import moinobudget.composeapp.generated.resources.label_citrus
import moinobudget.composeapp.generated.resources.label_waves
import moinobudget.composeapp.generated.resources.label_winter
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import presentation.data.BudgetStyleColors.Cyan
import presentation.data.BudgetStyleColors.LightBlue
import presentation.data.BudgetStyleColors.Purple
import presentation.data.BudgetStyleColors.RedPink
import presentation.data.BudgetStyleColors.Yellow

enum class BudgetStyle(val id: Int, val title: StringResource, val background: Background) {
    Wave(id = 1, title = Res.string.label_waves, Background.Image(Res.drawable.bg_card_1)),
    Winter(id = 2, title = Res.string.label_winter, Background.Gradient(listOf(Cyan, LightBlue, Purple))),
    CitrusJuice(id = 3, title = Res.string.label_citrus, Background.Gradient(listOf(Yellow, RedPink)));
    companion object {
        val list = listOf(Wave, Winter, CitrusJuice)
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