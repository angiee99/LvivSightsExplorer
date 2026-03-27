package com.angelina.lvivexplorer.feature.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angelina.lvivexplorer.domain.model.DiaryEntry
import com.angelina.lvivexplorer.domain.model.DiaryStatus
import com.angelina.lvivexplorer.domain.repository.DiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository
) : ViewModel() {
    val visitedEntries: StateFlow<List<DiaryEntry>> = diaryRepository.observeByStatus(DiaryStatus.VISITED)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val wantToVisitEntries: StateFlow<List<DiaryEntry>> = diaryRepository.observeByStatus(DiaryStatus.WANT_TO_VISIT)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun deleteEntry(id: Long) {
        viewModelScope.launch { diaryRepository.delete(id) }
    }

    fun updateNote(id: Long, note: String?, visitedAt: Long?) {
        viewModelScope.launch { diaryRepository.updateNote(id, note, visitedAt) }
    }
}
