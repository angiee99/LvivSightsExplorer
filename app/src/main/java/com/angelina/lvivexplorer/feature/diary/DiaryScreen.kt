package com.angelina.lvivexplorer.feature.diary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.angelina.lvivexplorer.domain.model.DiaryEntry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryScreen(
    viewModel: DiaryViewModel,
    onOpenDetails: (String) -> Unit
) {
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
                canEditVisitedAt = selectedTab == 0,
                onOpenDetails = onOpenDetails
            )
        }
    }
}

@Composable
private fun DiaryEntryList(
    entries: List<DiaryEntry>,
    onDelete: (Long) -> Unit,
    onUpdateNote: (Long, String?, Long?) -> Unit,
    canEditVisitedAt: Boolean,
    onOpenDetails: (String) -> Unit
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
            var note by rememberSaveable(entry.id) { mutableStateOf(entry.note.orEmpty()) }
            var expanded by rememberSaveable(entry.id) { mutableStateOf(false) }
            var editing by rememberSaveable(entry.id) { mutableStateOf(false) }

            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = entry.placeName,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (expanded) "Collapse note" else "Expand note"
                        )
                    }
                }

                if (expanded) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(start = 12.dp, end = 6.dp, bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            if (editing) {
                                OutlinedTextField(
                                    value = note,
                                    onValueChange = { note = it },
                                    label = { Text("Note") },
                                    modifier = Modifier.fillMaxWidth().heightIn(min = 96.dp)
                                )
                            } else {
                                Card(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = note.ifBlank { "No note yet." },
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                            }
                        }
                        Column {
                            TextButton(onClick = { onOpenDetails(entry.placeId) }) {
                                Text("Details")
                            }
                            IconButton(
                                onClick = {
                                    if (editing) {
                                        onUpdateNote(
                                            entry.id,
                                            note.ifBlank { null },
                                            if (canEditVisitedAt) System.currentTimeMillis() else null
                                        )
                                        editing = false
                                    } else {
                                        editing = true
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit note")
                            }
                            IconButton(onClick = { onDelete(entry.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete entry")
                            }
                        }
                    }
                }
            }
        }
    }
}
