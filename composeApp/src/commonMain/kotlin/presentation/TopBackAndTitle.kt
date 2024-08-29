package presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.back_home_description
import org.jetbrains.compose.resources.stringResource

@Composable
fun TopBackAndTitle(
    title: String,
    goBack: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = goBack) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(Res.string.back_home_description)
            )
        }
        Text(modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.titleLarge,
            text = title
        )
    }
}