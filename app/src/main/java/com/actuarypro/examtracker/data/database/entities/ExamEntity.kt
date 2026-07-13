package com.actuarypro.examtracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exams")
data class ExamEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,           // e.g., "SOA Exam FM"
    val organizationCode: String, // e.g., "SOA", "IFOA", "ASSA", "ICAS"
    val description: String,
    val totalQuestions: Int,
    val passingScore: Int,      // Percentage needed to pass
    val duration: Int,          // Duration in minutes
    val createdAt: Long = System.currentTimeMillis()
)
