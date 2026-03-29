package com.angelina.lvivexplorer.feature.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
            Text(text = current.name)
            Surface(
                color = categoryColor(current.category),
                contentColor = Color.White
            ) {
                Text(
                    text = current.category,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
            Text(text = current.address)
            Text(text = current.description)
            Button(
                onClick = { onShowOnMap(current.id) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Show on map")
            }
            val isVisitedSaved = savedStatuses.contains(DiaryStatus.VISITED)
            val isWantToVisitSaved = savedStatuses.contains(DiaryStatus.WANT_TO_VISIT)
            val isFullySaved = isVisitedSaved && isWantToVisitSaved
            if (!isFullySaved) {
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Optional note") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Button(onClick = {
                viewModel.markVisited(note.ifBlank { null })
                onDone()
            }, enabled = !isVisitedSaved, modifier = Modifier.fillMaxWidth()) {
                Text(if (isVisitedSaved) "Marked as visited" else "Mark as visited")
            }
            Button(onClick = {
                viewModel.markWantToVisit(note.ifBlank { null })
                onDone()
            }, enabled = !isWantToVisitSaved, modifier = Modifier.fillMaxWidth()) {
                Text(if (isWantToVisitSaved) "Added to want to visit" else "Add to want to visit")
            }
        }
    }
}
