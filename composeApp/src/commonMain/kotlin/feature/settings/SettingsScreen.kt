package feature.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import data.repository.AppPreferences
import data.value.Currency
import data.value.Language
import data.value.Theme
import feature.settings.components.BooleanEntry
import feature.settings.components.InfoEntry
import feature.settings.components.ListEntry
import feature.settings.components.SectionTitle
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.about
import moinobudget.composeapp.generated.resources.about_credits
import moinobudget.composeapp.generated.resources.about_version
import moinobudget.composeapp.generated.resources.back_home_description
import moinobudget.composeapp.generated.resources.card_style
import moinobudget.composeapp.generated.resources.currency
import moinobudget.composeapp.generated.resources.decimal_mode
import moinobudget.composeapp.generated.resources.language
import moinobudget.composeapp.generated.resources.section_interface
import moinobudget.composeapp.generated.resources.settings
import moinobudget.composeapp.generated.resources.theme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import presentation.LabelBackground

@Composable
fun SettingsScreen(
    goBack: () -> Unit,
    preferences: AppPreferences,
    onEvent: (SettingsEvent) -> Unit
) = Column {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = goBack) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(Res.string.back_home_description))
        }
        Text(modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.titleLarge,
            text = stringResource(Res.string.settings))
    }

    SectionTitle(stringResource(Res.string.section_interface))

    ListEntry(title = stringResource(Res.string.currency),
        currentKey = preferences.currency.key,
        currentText = preferences.currency.sign,
        items = Currency.list.map { Triple(
            it.key,
            it.sign
        ) {
            Text(text = it.sign)
        } },
        onChange = { onEvent(SettingsEvent.ChangeCurrency(it))} )

    BooleanEntry(
        title = stringResource(Res.string.decimal_mode),
        value = preferences.decimalMode,
        onChange = { onEvent(SettingsEvent.ChangeDecimalMode(it)) }
    )

    ListEntry(title = stringResource(Res.string.theme),
        currentKey = preferences.theme.key,
        currentText = stringResource(preferences.theme.title),
        items = Theme.list.map { Triple(
            it.key,
            stringResource(it.title)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(it.color)
                    .border(width = 2.dp, color = Color.Black, shape = CircleShape)) } },
        onChange = { onEvent(SettingsEvent.ChangeTheme(it)) })

    ListEntry(title = stringResource(Res.string.language),
        currentKey = preferences.language.key,
        currentText = preferences.language.title,
        items = Language.list.map { Triple(
            it.key,
            it.title) {
                Image(modifier = Modifier
                    .size(40.dp)
                    .padding(4.dp)
                    .clip(CircleShape)
                    .border(width = 2.dp, color = Color.Black, shape = CircleShape),
                    painter = painterResource(it.flag),
                    contentDescription = it.title)
        } },
        onChange = { onEvent(SettingsEvent.ChangeLanguage(it)) })

    SectionTitle(stringResource(Res.string.about))
    InfoEntry(title = stringResource(Res.string.about_version), value = "v0.0.1")
    InfoEntry(title = stringResource(Res.string.about_credits), value = "MoineauFactory")
}