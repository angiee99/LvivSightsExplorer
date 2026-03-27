package com.angelina.lvivexplorer.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angelina.lvivexplorer.domain.model.DiaryStatus
import com.angelina.lvivexplorer.domain.model.Place
import com.angelina.lvivexplorer.domain.repository.DiaryRepository
import com.angelina.lvivexplorer.domain.repository.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PlaceDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val placeRepository: PlaceRepository,
    private val diaryRepository: DiaryRepository
) : ViewModel() {
    private val placeId: String = savedStateHandle["placeId"].orEmpty()

    private val _place = MutableStateFlow<Place?>(null)
    val place: StateFlow<Place?> = _place

    init {
        viewModelScope.launch {
            _place.value = placeRepository.getPlaceById(placeId)
        }
    }

    fun markVisited(note: String?) {
        viewModelScope.launch {
            diaryRepository.addOrUpdate(
                placeId = placeId,
                status = DiaryStatus.VISITED,
                note = note,
                visitedAt = System.currentTimeMillis()
            )
        }
    }

    fun markWantToVisit(note: String?) {
        viewModelScope.launch {
            diaryRepository.addOrUpdate(
                placeId = placeId,
                status = DiaryStatus.WANT_TO_VISIT,
                note = note,
                visitedAt = null
            )
        }
    }
}
