package feature.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SectionTitle(title: String) = Text(text = title,
    modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp),
    style = MaterialTheme.typography.headlineSmall,
    fontWeight = FontWeight.SemiBold,
    color = MaterialTheme.colorScheme.primary)

@Composable
fun InfoEntry(
    modifier: Modifier = Modifier,
    title: String,
    value: String
) = Row(
    modifier = modifier.fillMaxWidth().height(48.dp),
    verticalAlignment = Alignment.CenterVertically
) {
    Text(text = title,
        modifier = Modifier.padding(horizontal = 16.dp).weight(1f))
    Text(modifier = Modifier.padding(end = 16.dp),
        text = value,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.SemiBold)}

@Composable
fun BooleanEntry(
    title: String,
    value: Boolean,
    onChange: (Boolean) -> Unit
) = Row(
    modifier = Modifier.fillMaxWidth().height(48.dp),
    verticalAlignment = Alignment.CenterVertically
) {
    Text(text = title,
        modifier = Modifier.padding(horizontal = 16.dp).weight(1f))
    Switch(modifier = Modifier.padding(end = 16.dp),
        checked = value,
        onCheckedChange = { onChange(it) }
    )
}

@Composable
fun ListEntry(
    title: String,
    items: List<Triple<String, String, (@Composable () -> Unit)?>>, // Pair <key, title>
    currentKey: String,
    currentText: String,
    onChange: (String) -> Unit
) {
    var dialogState by remember { mutableStateOf(false) }
    if (dialogState) SelectionFromListDialog(
        title = title,
        items = items,
        currentKey = currentKey,
        onSelect = {
            onChange(it)
            dialogState = false },
        onDismiss = { dialogState = false }
    )

    InfoEntry(
        modifier = Modifier.clickable { dialogState = true },
        title = title,
        value = currentText
    )
}