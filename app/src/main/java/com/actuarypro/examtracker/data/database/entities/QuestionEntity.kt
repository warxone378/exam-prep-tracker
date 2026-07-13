package com.actuarypro.examtracker.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "questions",
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
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val examId: Int,
    val questionText: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val optionE: String? = null,
    val correctAnswer: String,  // A, B, C, D, or E
    val explanation: String,
    val topic: String,         // e.g., "Time Value of Money", "Probability"
    val difficulty: Int,       // 1-5 scale
    val timesAnswered: Int = 0,
    val correctCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
