package com.angelina.lvivexplorer.feature.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.angelina.lvivexplorer.core.ui.categoryColor
import com.angelina.lvivexplorer.domain.model.DiaryStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceDetailScreen(
    viewModel: PlaceDetailViewModel,
    onDone: () -> Unit,
    onShowOnMap: (String) -> Unit
) {
    val place by viewModel.place.collectAsStateWithLifecycle()
    val savedStatuses by viewModel.savedStatuses.collectAsStateWithLifecycle()
    var note by remember { mutableStateOf("") }
    val sectionShape = RoundedCornerShape(14.dp)

    Scaffold(
        topBar = { TopAppBar(title = { Text("Place details") }) }
    ) { padding ->
        val current = place
        if (current == null) {
            Text("Loading...", modifier = Modifier.padding(padding).padding(16.dp))
            return@Scaffold
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = current.name,
                    style = MaterialTheme.typography.headlineMedium
                )
                val categoryTint = categoryColor(current.category)
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedCard(
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, categoryTint),
                        colors = CardDefaults.outlinedCardColors(
                            containerColor = categoryTint.copy(alpha = 0.16f)
                        )
                    ) {
                        Text(
                            text = current.category,
                            color = categoryTint,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    TextButton(
                        onClick = { onShowOnMap(current.id) },
                        shape = CircleShape,
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                        ),
                        modifier = Modifier.padding(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = "Show on map"
                        )
                        Text(
                            text = current.address,
                            modifier = Modifier.padding(start = 6.dp)
                        )
                    }
                }
                Text(text = current.description)
            }

            Spacer(modifier = Modifier.weight(1f))

            val isVisitedSaved = savedStatuses.contains(DiaryStatus.VISITED)
            val isWantToVisitSaved = savedStatuses.contains(DiaryStatus.WANT_TO_VISIT)
            val isFullySaved = isVisitedSaved && isWantToVisitSaved

            LaunchedEffect(isFullySaved) {
                if (isFullySaved) note = ""
            }

            if (!isFullySaved) {
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Optional note") },
                    shape = sectionShape,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Button(
                onClick = {
                    viewModel.markVisited(note.ifBlank { null })
                    onDone()
                },
                enabled = !isVisitedSaved,
                shape = sectionShape,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isVisitedSaved) "Marked as visited" else "Mark as visited")
            }
            Button(
                onClick = {
                    viewModel.markWantToVisit(note.ifBlank { null })
                    onDone()
                },
                enabled = !isWantToVisitSaved,
                shape = sectionShape,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                ),
                border = BorderStroke(
                    1.dp,
                    if (isWantToVisitSaved) {
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isWantToVisitSaved) "Added to want to visit" else "Add to want to visit")
            }
        }
    }
}
