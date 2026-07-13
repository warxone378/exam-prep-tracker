package com.actuarypro.examtracker.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "flashcards",
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
data class FlashcardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val examId: Int,
    val front: String,         // Question or term
    val back: String,          // Answer or definition
    val topic: String,
    val difficulty: Int,       // 1-5 scale
    val easinessValue: Float = 2.5f, // For SM-2 spaced repetition algorithm
    val interval: Int = 1,     // Days before next review
    val repetitions: Int = 0,
    val nextReviewDate: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis()
)
