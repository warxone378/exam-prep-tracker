package com.actuarypro.examtracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey
    val examId: Int,
    val totalQuestionsAttempted: Int = 0,
    val totalCorrect: Int = 0,
    val averageScore: Float = 0f,
    val streakDays: Int = 0,
    val lastStudyDate: Long? = null,
    val totalStudyTimeMinutes: Int = 0,
    val goalScore: Int = 70,
    val lastUpdated: Long = System.currentTimeMillis()
)
