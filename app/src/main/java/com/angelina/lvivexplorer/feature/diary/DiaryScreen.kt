package com.angelina.lvivexplorer.feature.diary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.angelina.lvivexplorer.domain.model.DiaryEntry

@Composable
fun DiaryScreen(viewModel: DiaryViewModel) {
    val visited by viewModel.visitedEntries.collectAsStateWithLifecycle()
    val want by viewModel.wantToVisitEntries.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(topBar = { TopAppBar(title = { Text("My diary") }) }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Visited") })
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Want to visit") })
            }
            val entries = if (selectedTab == 0) visited else want
            DiaryEntryList(
                entries = entries,
                onDelete = viewModel::deleteEntry,
                onUpdateNote = viewModel::updateNote,
                canEditVisitedAt = selectedTab == 0
            )
        }
    }
}

@Composable
private fun DiaryEntryList(
    entries: List<DiaryEntry>,
    onDelete: (Long) -> Unit,
    onUpdateNote: (Long, String?, Long?) -> Unit,
    canEditVisitedAt: Boolean
) {
    if (entries.isEmpty()) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("No entries yet.")
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(entries, key = { it.id }) { entry ->
            var note by remember(entry.id) { mutableStateOf(entry.note.orEmpty()) }
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(entry.placeName)
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Note") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = {
                        onUpdateNote(
                            entry.id,
                            note.ifBlank { null },
                            if (canEditVisitedAt) System.currentTimeMillis() else null
                        )
                    }) { Text("Save note") }
                    Button(onClick = { onDelete(entry.id) }) { Text("Delete") }
                }
            }
        }
    }
}
