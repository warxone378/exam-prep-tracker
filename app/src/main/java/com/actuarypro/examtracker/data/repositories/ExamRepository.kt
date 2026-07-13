package com.actuarypro.examtracker.data.repositories

import com.actuarypro.examtracker.data.database.daos.*
import com.actuarypro.examtracker.data.database.entities.*
import kotlinx.coroutines.flow.Flow

class ExamRepository(
    private val examDao: ExamDao,
    private val questionDao: QuestionDao,
    private val userAnswerDao: UserAnswerDao,
    private val studySessionDao: StudySessionDao,
    private val userProgressDao: UserProgressDao,
    private val flashcardDao: FlashcardDao
) {
    // Exam operations
    fun getAllExams(): Flow<List<ExamEntity>> = examDao.getAllExams()
    fun getExamsByOrganization(orgCode: String): Flow<List<ExamEntity>> =
        examDao.getExamsByOrganization(orgCode)
    fun getAllOrganizations(): Flow<List<String>> = examDao.getAllOrganizations()
    suspend fun getExamById(examId: Int): ExamEntity? = examDao.getExamById(examId)

    // Question operations
    suspend fun getRandomQuestionsByExam(examId: Int, limit: Int): List<QuestionEntity> =
        questionDao.getRandomQuestionsByExam(examId, limit)
    suspend fun getRandomQuestionsByTopic(examId: Int, topic: String, limit: Int): List<QuestionEntity> =
        questionDao.getRandomQuestionsByTopic(examId, topic, limit)
    suspend fun getWeakAreaQuestions(examId: Int, limit: Int): List<QuestionEntity> =
        questionDao.getWeakAreaQuestions(examId, limit)
    fun getTopicsByExam(examId: Int): Flow<List<String>> = questionDao.getTopicsByExam(examId)
    fun getQuestionCountByExam(examId: Int): Flow<Int> = questionDao.getQuestionCountByExam(examId)

    // User answer operations
    suspend fun recordAnswer(answer: UserAnswerEntity) = userAnswerDao.insertAnswer(answer)
    suspend fun getOverallAccuracyForExam(examId: Int): Float? =
        userAnswerDao.getOverallAccuracyForExam(examId)

    // Study session operations
    suspend fun createStudySession(session: StudySessionEntity): Long =
        studySessionDao.insertSession(session)
    suspend fun updateStudySession(session: StudySessionEntity) =
        studySessionDao.updateSession(session)
    fun getSessionsByExam(examId: Int): Flow<List<StudySessionEntity>> =
        studySessionDao.getSessionsByExam(examId)
    suspend fun getAverageScoreForExam(examId: Int): Float? =
        studySessionDao.getAverageScoreForExam(examId)
    suspend fun getTotalStudyTimeForExam(examId: Int): Long? =
        studySessionDao.getTotalStudyTimeForExam(examId)

    // User progress operations
    fun getProgressByExam(examId: Int): Flow<UserProgressEntity?> =
        userProgressDao.getProgressByExam(examId)
    suspend fun updateProgress(progress: UserProgressEntity) =
        userProgressDao.updateProgress(progress)

    // Flashcard operations
    fun getFlashcardsByExam(examId: Int): Flow<List<FlashcardEntity>> =
        flashcardDao.getFlashcardsByExam(examId)
    suspend fun getDueFlashcards(examId: Int, limit: Int = 10): List<FlashcardEntity> =
        flashcardDao.getDueFlashcards(examId, System.currentTimeMillis(), limit)
    fun getDueFlashcardCount(examId: Int): Flow<Int> =
        flashcardDao.getDueFlashcardCount(examId, System.currentTimeMillis())
    fun getFlashcardTopics(examId: Int): Flow<List<String>> = flashcardDao.getTopicsByExam(examId)
    suspend fun updateFlashcard(flashcard: FlashcardEntity) = flashcardDao.updateFlashcard(flashcard)
}
