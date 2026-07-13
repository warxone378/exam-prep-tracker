package com.actuarypro.examtracker.data.database.daos

import androidx.room.*
import com.actuarypro.examtracker.data.database.entities.ExamEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExamDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExam(exam: ExamEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExams(exams: List<ExamEntity>)

    @Query("SELECT * FROM exams WHERE id = :examId")
    suspend fun getExamById(examId: Int): ExamEntity?

    @Query("SELECT * FROM exams WHERE organizationCode = :orgCode")
    fun getExamsByOrganization(orgCode: String): Flow<List<ExamEntity>>

    @Query("SELECT * FROM exams ORDER BY name ASC")
    fun getAllExams(): Flow<List<ExamEntity>>

    @Query("SELECT DISTINCT organizationCode FROM exams ORDER BY organizationCode")
    fun getAllOrganizations(): Flow<List<String>>

    @Update
    suspend fun updateExam(exam: ExamEntity)

    @Delete
    suspend fun deleteExam(exam: ExamEntity)

    @Query("DELETE FROM exams")
    suspend fun deleteAllExams()
}
