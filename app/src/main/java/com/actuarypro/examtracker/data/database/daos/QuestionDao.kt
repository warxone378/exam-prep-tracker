package com.actuarypro.examtracker.data.database.daos

import androidx.room.*
import com.actuarypro.examtracker.data.database.entities.QuestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: QuestionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuestionEntity>)

    @Query("SELECT * FROM questions WHERE id = :questionId")
    suspend fun getQuestionById(questionId: Int): QuestionEntity?

    @Query("SELECT * FROM questions WHERE examId = :examId ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomQuestionsByExam(examId: Int, limit: Int): List<QuestionEntity>

    @Query("SELECT * FROM questions WHERE examId = :examId AND topic = :topic ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomQuestionsByTopic(examId: Int, topic: String, limit: Int): List<QuestionEntity>

    @Query("SELECT * FROM questions WHERE examId = :examId ORDER BY difficulty ASC LIMIT :limit")
    suspend fun getEasyQuestionsByExam(examId: Int, limit: Int): List<QuestionEntity>

    @Query("SELECT * FROM questions WHERE examId = :examId ORDER BY difficulty DESC LIMIT :limit")
    suspend fun getDifficultQuestionsByExam(examId: Int, limit: Int): List<QuestionEntity>

    @Query("SELECT DISTINCT topic FROM questions WHERE examId = :examId ORDER BY topic")
    fun getTopicsByExam(examId: Int): Flow<List<String>>

    @Query("SELECT COUNT(*) FROM questions WHERE examId = :examId")
    fun getQuestionCountByExam(examId: Int): Flow<Int>

    @Query("SELECT * FROM questions WHERE examId = :examId AND correctCount < timesAnswered ORDER BY correctCount / CAST(timesAnswered AS FLOAT) ASC LIMIT :limit")
    suspend fun getWeakAreaQuestions(examId: Int, limit: Int): List<QuestionEntity>

    @Update
    suspend fun updateQuestion(question: QuestionEntity)

    @Delete
    suspend fun deleteQuestion(question: QuestionEntity)

    @Query("DELETE FROM questions WHERE examId = :examId")
    suspend fun deleteQuestionsByExam(examId: Int)
}
