package com.angelina.lvivexplorer.feature.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel,
    onOpenFilter: () -> Unit,
    onOpenDetails: (String) -> Unit
) {
    val places by viewModel.places.collectAsStateWithLifecycle()
    val selectedCategories by viewModel.selectedCategoriesState.collectAsStateWithLifecycle()

    val lvivCenter = LatLng(49.8397, 24.0297)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(lvivCenter, 13f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (selectedCategories.isEmpty()) {
                            "Lviv Architecture Explorer"
                        } else {
                            "Filters: ${selectedCategories.size}"
                        }
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onOpenFilter) {
                Icon(Icons.Default.FilterList, contentDescription = "Open filters")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                places.forEach { place ->
                    Marker(
                        state = MarkerState(position = LatLng(place.latitude, place.longitude)),
                        title = place.name,
                        snippet = place.category,
                        onClick = {
                            onOpenDetails(place.id)
                            true
                        }
                    )
                }
            }
            if (places.isEmpty()) {
                Text(
                    text = "No places match selected filters",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
