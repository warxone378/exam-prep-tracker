package com.actuarypro.examtracker.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "user_answers",
    foreignKeys = [
        ForeignKey(
            entity = QuestionEntity::class,
            parentColumns = ["id"],
            childColumns = ["questionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("questionId")]
)
data class UserAnswerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val questionId: Int,
    val selectedAnswer: String,
    val isCorrect: Boolean,
    val timeSpentSeconds: Int,
    val attemptNumber: Int = 1,
    val answeredAt: Long = System.currentTimeMillis()
)
