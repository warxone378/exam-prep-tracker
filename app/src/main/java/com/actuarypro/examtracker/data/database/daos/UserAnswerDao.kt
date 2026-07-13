package com.actuarypro.examtracker.data.database.daos

import androidx.room.*
import com.actuarypro.examtracker.data.database.entities.UserAnswerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserAnswerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswer(answer: UserAnswerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswers(answers: List<UserAnswerEntity>)

    @Query("SELECT * FROM user_answers WHERE questionId = :questionId ORDER BY answeredAt DESC")
    fun getAnswersByQuestion(questionId: Int): Flow<List<UserAnswerEntity>>

    @Query("SELECT COUNT(*) FROM user_answers WHERE questionId = :questionId AND isCorrect = 1")
    suspend fun getCorrectAnswerCountForQuestion(questionId: Int): Int

    @Query("SELECT COUNT(*) FROM user_answers WHERE questionId = :questionId")
    suspend fun getTotalAnswerCountForQuestion(questionId: Int): Int

    @Query("SELECT AVG(CAST(isCorrect AS FLOAT)) FROM user_answers WHERE questionId = :questionId")
    suspend fun getAccuracyForQuestion(questionId: Int): Float?

    @Query("""SELECT AVG(CAST(isCorrect AS FLOAT)) FROM user_answers ua
        JOIN questions q ON ua.questionId = q.id
        WHERE q.examId = :examId""")
    suspend fun getOverallAccuracyForExam(examId: Int): Float?

    @Query("SELECT * FROM user_answers ORDER BY answeredAt DESC LIMIT :limit")
    fun getRecentAnswers(limit: Int): Flow<List<UserAnswerEntity>>

    @Update
    suspend fun updateAnswer(answer: UserAnswerEntity)

    @Delete
    suspend fun deleteAnswer(answer: UserAnswerEntity)

    @Query("DELETE FROM user_answers WHERE questionId = :questionId")
    suspend fun deleteAnswersByQuestion(questionId: Int)
}
