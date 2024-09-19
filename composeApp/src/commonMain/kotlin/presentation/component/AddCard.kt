package presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import presentation.dashedBorder

@Composable
fun AddCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    onClick: () -> Unit
) = Row(modifier
    .fillMaxWidth()
    .padding(top = 12.dp, bottom = 24.dp)
    .dashedBorder(4.dp, MaterialTheme.colorScheme.primary, 16.dp)
    .clip(RoundedCornerShape(16.dp))
    .background(MaterialTheme.colorScheme.surface)
    .clickable { onClick() }
    .padding(16.dp),
    verticalAlignment = Alignment.CenterVertically
) {
    Icon(modifier = Modifier
        .size(40.dp)
        .clip(CircleShape)
        .background(MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.3f)),
        tint = MaterialTheme.colorScheme.primary,
        imageVector = Icons.Default.Add,
        contentDescription = description
    )
    Column(Modifier.padding(start = 16.dp)) {
        Text(
            title,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleMedium)
        Text(
            description,
            fontWeight = FontWeight.Normal,
            style = MaterialTheme.typography.titleSmall)
    }
}