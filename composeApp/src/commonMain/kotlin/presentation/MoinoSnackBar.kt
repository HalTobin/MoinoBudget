package presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.dismiss_message
import org.jetbrains.compose.resources.stringResource

@Composable
fun MoinoSnackBar(data: SnackbarData) = Row(
    Modifier
    .fillMaxWidth()
    .padding(horizontal = 16.dp)
    .clip(RoundedCornerShape(8.dp))
    .background(MaterialTheme.colorScheme.surface)
    .padding(start = 12.dp, top = 6.dp, bottom = 6.dp),
    verticalAlignment = Alignment.CenterVertically) {
    Text(data.visuals.message, modifier = Modifier.weight(1f))
    IconButton(onClick = { data.dismiss() }) {
        Icon(Icons.Default.Close, contentDescription = stringResource(Res.string.dismiss_message))
    }
}