package presentation.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.edit
import org.jetbrains.compose.resources.stringResource

@Composable
fun AddFloatingButton(
    onClick: () -> Unit,
    text: String
) = ExtendedFloatingActionButton(
    containerColor = MaterialTheme.colorScheme.primary,
    contentColor = MaterialTheme.colorScheme.onPrimary,
    text = { Text(text) },
    icon = { Icon(Icons.Default.Add, contentDescription = null) },
    onClick = onClick
)

@Composable
fun EditFloatingButton(
    onClick: () -> Unit,
    text: String = stringResource(Res.string.edit)
) = ExtendedFloatingActionButton(
    containerColor = MaterialTheme.colorScheme.primary,
    contentColor = MaterialTheme.colorScheme.onPrimary,
    text = { Text(text) },
    icon = { Icon(Icons.Default.Edit, contentDescription = null) },
    onClick = onClick
)