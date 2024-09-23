package feature.settings.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
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
    icon: ImageVector?,
    value: String,
    style: EntryStyle = EntryStyle.Default
) = Row(
    modifier = modifier.fillMaxWidth().height(48.dp),
    verticalAlignment = Alignment.CenterVertically
) {
    IconEntry(icon, title)
    Text(text = title,
        modifier = Modifier.padding(horizontal = 16.dp).weight(1f))
    when (style) {
        EntryStyle.Default -> Text(modifier = Modifier.padding(end = 16.dp),
            text = value,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold)
        EntryStyle.List -> Row(Modifier
            .padding(end = 16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            AnimatedContent(
                targetState = value,
                transitionSpec = { slideInHorizontally() + fadeIn() togetherWith slideOutHorizontally() + fadeOut() }
            ) { currentValue ->
                Text(modifier = Modifier.padding(horizontal = 8.dp).animateContentSize(),
                    text = currentValue,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold)
            }
            Icon(Icons.Default.ArrowDropDown,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null)
        }
    }
}

@Composable
fun BooleanEntry(
    title: String,
    icon: ImageVector?,
    value: Boolean,
    onChange: (Boolean) -> Unit
) = Row(
    modifier = Modifier.fillMaxWidth().height(48.dp),
    verticalAlignment = Alignment.CenterVertically
) {
    IconEntry(icon, title)
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
    icon: ImageVector?,
    items: List<Triple<String, String, (@Composable () -> Unit)?>>, // Pair <key, title>
    currentKey: String,
    currentText: String,
    onChange: (String) -> Unit,
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
        icon = icon,
        title = title,
        value = currentText,
        style = EntryStyle.List
    )
}

@Composable
fun IconEntry(icon: ImageVector?, title: String) = icon?.let {
    Spacer(Modifier.width(16.dp))
    Icon(it, contentDescription = title)
} ?: Spacer(Modifier.width(40.dp))

enum class EntryStyle { Default, List }