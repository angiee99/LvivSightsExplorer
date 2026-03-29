package com.angelina.lvivexplorer.feature.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angelina.lvivexplorer.domain.model.Place
import com.angelina.lvivexplorer.domain.repository.PlaceRepository
import com.angelina.lvivexplorer.domain.usecase.SeedPlacesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class MapViewModel @Inject constructor(
    private val placeRepository: PlaceRepository,
    private val seedPlacesUseCase: SeedPlacesUseCase
) : ViewModel() {

    private val selectedCategories = MutableStateFlow<Set<String>>(emptySet())

    val places: StateFlow<List<Place>> = selectedCategories
        .flatMapLatest { selected -> placeRepository.observePlaces(selected) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categories: StateFlow<List<String>> = placeRepository.observeCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val selectedCategoriesState: StateFlow<Set<String>> = selectedCategories

    init {
        viewModelScope.launch {
            seedPlacesUseCase()
        }
    }

    fun toggleCategory(category: String) {
        selectedCategories.value = selectedCategories.value.toMutableSet().also { mutable ->
            if (!mutable.add(category)) mutable.remove(category)
        }
    }

    fun clearFilters() {
        selectedCategories.value = emptySet()
    }

    fun setAllFilters(categories: List<String>, selected: Boolean) {
        selectedCategories.value = if (selected) categories.toSet() else emptySet()
    }
}
