package com.actuarypro.examtracker.data.database.daos

import androidx.room.*
import com.actuarypro.examtracker.data.database.entities.UserProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProgressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: UserProgressEntity): Long

    @Query("SELECT * FROM user_progress WHERE examId = :examId")
    fun getProgressByExam(examId: Int): Flow<UserProgressEntity?>

    @Query("SELECT * FROM user_progress ORDER BY lastUpdated DESC")
    fun getAllProgress(): Flow<List<UserProgressEntity>>

    @Update
    suspend fun updateProgress(progress: UserProgressEntity)

    @Delete
    suspend fun deleteProgress(progress: UserProgressEntity)

    @Query("DELETE FROM user_progress")
    suspend fun deleteAllProgress()
}
