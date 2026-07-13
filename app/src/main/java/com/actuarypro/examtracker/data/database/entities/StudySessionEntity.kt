package com.actuarypro.examtracker.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "study_sessions",
    foreignKeys = [
        ForeignKey(
            entity = ExamEntity::class,
            parentColumns = ["id"],
            childColumns = ["examId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("examId")]
)
data class StudySessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val examId: Int,
    val sessionType: String,    // "QUIZ", "FLASHCARD", "FULL_EXAM"
    val questionsAttempted: Int,
    val questionsCorrect: Int,
    val score: Float,           // Percentage
    val durationSeconds: Int,
    val startedAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null
)
