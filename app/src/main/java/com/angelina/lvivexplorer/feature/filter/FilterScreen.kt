package com.angelina.lvivexplorer.feature.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.angelina.lvivexplorer.core.ui.categoryColor
import com.angelina.lvivexplorer.feature.map.MapViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    viewModel: MapViewModel,
    onApplyAndBack: () -> Unit
) {
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val selected by viewModel.selectedCategoriesState.collectAsStateWithLifecycle()
    val allSelected = categories.isNotEmpty() && selected.size == categories.size

    Scaffold(
        topBar = { TopAppBar(title = { Text("Filter categories") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Select all categories")
                Checkbox(
                    checked = allSelected,
                    onCheckedChange = { checked ->
                        viewModel.setAllFilters(categories, checked)
                    }
                )
            }
            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { category ->
                    val isChecked = selected.contains(category)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = category, color = categoryColor(category))
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { viewModel.toggleCategory(category) }
                        )
                    }
                }
            }
            Button(
                onClick = onApplyAndBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Apply")
            }
        }
    }
}
