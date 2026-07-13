package com.actuarypro.examtracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actuarypro.examtracker.data.database.entities.FlashcardEntity
import com.actuarypro.examtracker.data.repositories.ExamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.min
import kotlin.math.max
import javax.inject.Inject

@HiltViewModel
class FlashcardViewModel @Inject constructor(
    private val repository: ExamRepository
) : ViewModel() {
    private val _flashcards = MutableStateFlow<List<FlashcardEntity>>(emptyList())
    val flashcards: StateFlow<List<FlashcardEntity>> = _flashcards.asStateFlow()

    private val _currentFlashcardIndex = MutableStateFlow(0)
    val currentFlashcardIndex: StateFlow<Int> = _currentFlashcardIndex.asStateFlow()

    private val _isFlipped = MutableStateFlow(false)
    val isFlipped: StateFlow<Boolean> = _isFlipped.asStateFlow()

    private val _studyComplete = MutableStateFlow(false)
    val studyComplete: StateFlow<Boolean> = _studyComplete.asStateFlow()

    fun loadFlashcards(examId: Int) {
        viewModelScope.launch {
            val dueCards = repository.getDueFlashcards(examId, 10)
            _flashcards.value = dueCards
            _currentFlashcardIndex.value = 0
            _isFlipped.value = false
        }
    }

    fun flipCard() {
        _isFlipped.value = !_isFlipped.value
    }

    fun markAsEasy() {
        updateFlashcard(1)
    }

    fun markAsGood() {
        updateFlashcard(2)
    }

    fun markAsHard() {
        updateFlashcard(3)
    }

    private fun updateFlashcard(quality: Int) {
        viewModelScope.launch {
            val currentCard = _flashcards.value.getOrNull(_currentFlashcardIndex.value)
            if (currentCard != null) {
                // SM-2 Spaced Repetition Algorithm
                val easiness = calculateEasiness(currentCard.easinessValue, quality)
                val interval = calculateInterval(currentCard.interval, currentCard.repetitions, easiness)
                val nextReviewDate = System.currentTimeMillis() + (interval * 24 * 60 * 60 * 1000)

                val updated = currentCard.copy(
                    easinessValue = easiness,
                    interval = interval,
                    repetitions = currentCard.repetitions + 1,
                    nextReviewDate = nextReviewDate
                )
                repository.updateFlashcard(updated)
            }
            moveToNextFlashcard()
        }
    }

    private fun calculateEasiness(currentEasiness: Float, quality: Int): Float {
        val ef = currentEasiness + (0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02))
        return max(1.3f, ef)
    }

    private fun calculateInterval(currentInterval: Int, repetitions: Int, easiness: Float): Int {
        return when (repetitions) {
            0 -> 1
            1 -> 3
            else -> (currentInterval * easiness).toInt()
        }
    }

    private fun moveToNextFlashcard() {
        val nextIndex = _currentFlashcardIndex.value + 1
        if (nextIndex >= _flashcards.value.size) {
            _studyComplete.value = true
        } else {
            _currentFlashcardIndex.value = nextIndex
            _isFlipped.value = false
        }
    }

    fun getCurrentFlashcard(): FlashcardEntity? {
        return _flashcards.value.getOrNull(_currentFlashcardIndex.value)
    }
}
