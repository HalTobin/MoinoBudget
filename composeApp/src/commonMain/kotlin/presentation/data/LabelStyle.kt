package presentation.data

import androidx.compose.ui.graphics.Color
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.bg_card_1
import moinobudget.composeapp.generated.resources.label_citrus
import moinobudget.composeapp.generated.resources.label_waves
import moinobudget.composeapp.generated.resources.label_winter
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import presentation.data.LabelStyleColors.Cyan
import presentation.data.LabelStyleColors.LightBlue
import presentation.data.LabelStyleColors.Purple
import presentation.data.LabelStyleColors.RedPink
import presentation.data.LabelStyleColors.Yellow

enum class LabelStyle(val id: Int, val title: StringResource, val background: Background) {
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

object LabelStyleColors {
    val Cyan = Color(0xFF00FFFF)
    val LightBlue = Color(0xFF6666AA)
    val Purple = Color(0xFFFF00FF)

    val Yellow = Color(0xFFFDD835)
    val RedPink = Color(0xFFFA2A55)
    //val Orange = Color(0xFFFFEB3B)
}