package com.actuarypro.examtracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actuarypro.examtracker.data.database.entities.UserProgressEntity
import com.actuarypro.examtracker.data.repositories.ExamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: ExamRepository
) : ViewModel() {
    private val _userProgress = MutableStateFlow<UserProgressEntity?>(null)
    val userProgress: StateFlow<UserProgressEntity?> = _userProgress.asStateFlow()

    private val _averageScore = MutableStateFlow(0f)
    val averageScore: StateFlow<Float> = _averageScore.asStateFlow()

    private val _totalStudyTime = MutableStateFlow(0L)
    val totalStudyTime: StateFlow<Long> = _totalStudyTime.asStateFlow()

    private val _dueFlashcardCount = MutableStateFlow(0)
    val dueFlashcardCount: StateFlow<Int> = _dueFlashcardCount.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadDashboardData(examId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getProgressByExam(examId).collect { progress ->
                _userProgress.value = progress ?: UserProgressEntity(examId = examId)
                _isLoading.value = false
            }
        }

        viewModelScope.launch {
            val avgScore = repository.getAverageScoreForExam(examId)
            _averageScore.value = avgScore ?: 0f
        }

        viewModelScope.launch {
            val studyTime = repository.getTotalStudyTimeForExam(examId)
            _totalStudyTime.value = studyTime ?: 0L
        }

        viewModelScope.launch {
            repository.getDueFlashcardCount(examId).collect { count ->
                _dueFlashcardCount.value = count
            }
        }
    }

    fun updateProgress(examId: Int, score: Float, studyTimeMinutes: Int) {
        viewModelScope.launch {
            val currentProgress = _userProgress.value ?: UserProgressEntity(examId = examId)
            val updated = currentProgress.copy(
                totalQuestionsAttempted = currentProgress.totalQuestionsAttempted + 1,
                averageScore = score,
                totalStudyTimeMinutes = currentProgress.totalStudyTimeMinutes + studyTimeMinutes,
                lastUpdated = System.currentTimeMillis()
            )
            repository.updateProgress(updated)
            _userProgress.value = updated
        }
    }
}
