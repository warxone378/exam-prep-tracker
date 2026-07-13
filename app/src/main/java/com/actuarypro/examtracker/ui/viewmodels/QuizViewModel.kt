package com.actuarypro.examtracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actuarypro.examtracker.data.database.entities.*
import com.actuarypro.examtracker.data.repositories.ExamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: ExamRepository
) : ViewModel() {
    private val _questions = MutableStateFlow<List<QuestionEntity>>(emptyList())
    val questions: StateFlow<List<QuestionEntity>> = _questions.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    private val _isQuizComplete = MutableStateFlow(false)
    val isQuizComplete: StateFlow<Boolean> = _isQuizComplete.asStateFlow()

    private val _sessionId = MutableStateFlow<Int?>(null)
    val sessionId: StateFlow<Int?> = _sessionId.asStateFlow()

    private var startTime = 0L
    private var currentSession: StudySessionEntity? = null

    fun startQuiz(examId: Int, limit: Int = 10, topic: String? = null) {
        viewModelScope.launch {
            startTime = System.currentTimeMillis()
            val questions = if (topic != null) {
                repository.getRandomQuestionsByTopic(examId, topic, limit)
            } else {
                repository.getRandomQuestionsByExam(examId, limit)
            }
            _questions.value = questions
            _currentQuestionIndex.value = 0
            _score.value = 0
            _isQuizComplete.value = false
        }
    }

    fun selectAnswer(selectedAnswer: String, isCorrect: Boolean) {
        viewModelScope.launch {
            val currentQuestion = _questions.value.getOrNull(_currentQuestionIndex.value)
            if (currentQuestion != null) {
                val answer = UserAnswerEntity(
                    questionId = currentQuestion.id,
                    selectedAnswer = selectedAnswer,
                    isCorrect = isCorrect,
                    timeSpentSeconds = 30 // Placeholder - implement actual timer
                )
                repository.recordAnswer(answer)
                if (isCorrect) {
                    _score.value += 1
                }
            }
            moveToNextQuestion()
        }
    }

    private fun moveToNextQuestion() {
        val nextIndex = _currentQuestionIndex.value + 1
        if (nextIndex >= _questions.value.size) {
            completeQuiz()
        } else {
            _currentQuestionIndex.value = nextIndex
        }
    }

    private fun completeQuiz() {
        viewModelScope.launch {
            _isQuizComplete.value = true
            val endTime = System.currentTimeMillis()
            val durationSeconds = ((endTime - startTime) / 1000).toInt()
            val scorePercentage = (_score.value.toFloat() / _questions.value.size) * 100

            val session = StudySessionEntity(
                examId = 1, // TODO: Pass examId properly
                sessionType = "QUIZ",
                questionsAttempted = _questions.value.size,
                questionsCorrect = _score.value,
                score = scorePercentage,
                durationSeconds = durationSeconds,
                completedAt = System.currentTimeMillis()
            )
            currentSession = session
            val id = repository.createStudySession(session)
            _sessionId.value = id.toInt()
        }
    }

    fun getCurrentQuestion(): QuestionEntity? {
        return _questions.value.getOrNull(_currentQuestionIndex.value)
    }
}
