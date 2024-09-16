package data.mapper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import data.db.table.Label
import feature.budgets.data.LabelUI

fun Label.toLabelUI(): LabelUI =
    LabelUI(
        id = this.id,
        name = this.name,
        color = Color(this.color)
    )

fun LabelUI.toLabelEntity(): Label =
    Label(
        id = this.id,
        name = this.name,
        color = this.color.toArgb()
    )