package com.actuarypro.examtracker.data.database.daos

import androidx.room.*
import com.actuarypro.examtracker.data.database.entities.StudySessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudySessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: StudySessionEntity): Long

    @Query("SELECT * FROM study_sessions WHERE id = :sessionId")
    suspend fun getSessionById(sessionId: Int): StudySessionEntity?

    @Query("SELECT * FROM study_sessions WHERE examId = :examId ORDER BY startedAt DESC")
    fun getSessionsByExam(examId: Int): Flow<List<StudySessionEntity>>

    @Query("SELECT * FROM study_sessions WHERE examId = :examId AND sessionType = :sessionType ORDER BY startedAt DESC")
    fun getSessionsByExamAndType(examId: Int, sessionType: String): Flow<List<StudySessionEntity>>

    @Query("""SELECT * FROM study_sessions 
        WHERE examId = :examId AND completedAt IS NOT NULL
        ORDER BY startedAt DESC LIMIT :limit""")
    fun getCompletedSessions(examId: Int, limit: Int): Flow<List<StudySessionEntity>>

    @Query("""SELECT AVG(score) FROM study_sessions 
        WHERE examId = :examId AND completedAt IS NOT NULL""")
    suspend fun getAverageScoreForExam(examId: Int): Float?

    @Query("""SELECT SUM(durationSeconds) FROM study_sessions 
        WHERE examId = :examId AND completedAt IS NOT NULL""")
    suspend fun getTotalStudyTimeForExam(examId: Int): Long?

    @Query("""SELECT * FROM study_sessions 
        WHERE examId = :examId 
        AND date(startedAt / 1000, 'unixepoch') = date('now')
        ORDER BY startedAt DESC""")
    fun getTodaysSessions(examId: Int): Flow<List<StudySessionEntity>>

    @Update
    suspend fun updateSession(session: StudySessionEntity)

    @Delete
    suspend fun deleteSession(session: StudySessionEntity)
}
