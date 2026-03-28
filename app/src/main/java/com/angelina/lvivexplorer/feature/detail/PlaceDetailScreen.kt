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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceDetailScreen(
    viewModel: PlaceDetailViewModel,
    onDone: () -> Unit
) {
    val place by viewModel.place.collectAsStateWithLifecycle()
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
            Text(text = current.category)
            Text(text = current.address)
            Text(text = current.description)
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Optional note") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(onClick = {
                viewModel.markVisited(note.ifBlank { null })
                onDone()
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Mark as visited")
            }
            Button(onClick = {
                viewModel.markWantToVisit(note.ifBlank { null })
                onDone()
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Add to want to visit")
            }
        }
    }
}
