package com.angelina.lvivexplorer.feature.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.viewinterop.AndroidView
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import com.angelina.lvivexplorer.core.ui.categoryArgb
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel,
    focusPlaceId: String?,
    onOpenFilter: () -> Unit,
    onOpenDetails: (String) -> Unit
) {
    val places by viewModel.places.collectAsStateWithLifecycle()
    val selectedCategories by viewModel.selectedCategoriesState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val lvivCenter = GeoPoint(49.8397, 24.0297)
    var lastFocusedPlaceId by remember { mutableStateOf<String?>(null) }

    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            controller.setZoom(15.0)
            controller.setCenter(lvivCenter)
        }
    }

    DisposableEffect(lifecycleOwner, mapView) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_DESTROY -> mapView.onDetach()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView.onDetach()
        }
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
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { mapView },
                update = { view ->
                    view.overlays.removeAll { it is Marker }
                    val focusTarget = focusPlaceId?.let { id -> places.firstOrNull { it.id == id } }
                    if (focusTarget != null && lastFocusedPlaceId != focusTarget.id) {
                        view.controller.setZoom(16.0)
                        view.controller.setCenter(GeoPoint(focusTarget.latitude, focusTarget.longitude))
                        lastFocusedPlaceId = focusTarget.id
                    }

                    places.forEach { place ->
                        val marker = Marker(view).apply {
                            position = GeoPoint(place.latitude, place.longitude)
                            title = place.name
                            subDescription = place.category
                            icon = AppCompatResources.getDrawable(context, org.osmdroid.library.R.drawable.marker_default)
                                ?.mutate()
                                ?.let { drawable ->
                                    DrawableCompat.setTint(drawable, categoryArgb(place.category))
                                    drawable
                                }
                            setOnMarkerClickListener { _, _ ->
                                onOpenDetails(place.id)
                                true
                            }
                        }
                        view.overlays.add(marker)
                    }
                    view.invalidate()
                }
            )
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
