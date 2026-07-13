package com.actuarypro.examtracker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.actuarypro.examtracker.data.database.daos.*
import com.actuarypro.examtracker.data.database.entities.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [
        ExamEntity::class,
        QuestionEntity::class,
        UserAnswerEntity::class,
        StudySessionEntity::class,
        UserProgressEntity::class,
        FlashcardEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun examDao(): ExamDao
    abstract fun questionDao(): QuestionDao
    abstract fun userAnswerDao(): UserAnswerDao
    abstract fun studySessionDao(): StudySessionDao
    abstract fun userProgressDao(): UserProgressDao
    abstract fun flashcardDao(): FlashcardDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "exam_prep_database"
                )
                    .setJournalMode(JournalMode.WRITE_AHEAD_LOGGING)
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database)
                }
            }
        }

        suspend fun populateDatabase(database: AppDatabase) {
            val examDao = database.examDao()
            // Clear existing data
            examDao.deleteAllExams()

            // Populate with initial actuarial exam data
            val exams = listOf(
                ExamEntity(
                    name = "SOA Exam FM (Financial Mathematics)",
                    organizationCode = "SOA",
                    description = "Society of Actuaries - Financial Mathematics",
                    totalQuestions = 35,
                    passingScore = 70,
                    duration = 3 * 60
                ),
                ExamEntity(
                    name = "SOA Exam LTAM (Long-Term Actuarial Mathematics)",
                    organizationCode = "SOA",
                    description = "Society of Actuaries - Long-Term Actuarial Mathematics",
                    totalQuestions = 35,
                    passingScore = 70,
                    duration = 3 * 60
                ),
                ExamEntity(
                    name = "SOA Exam STAM (Short-Term Actuarial Mathematics)",
                    organizationCode = "SOA",
                    description = "Society of Actuaries - Short-Term Actuarial Mathematics",
                    totalQuestions = 35,
                    passingScore = 70,
                    duration = 3 * 60
                ),
                ExamEntity(
                    name = "IFOA Exam CT1 (Financial Mathematics)",
                    organizationCode = "IFOA",
                    description = "Institute and Faculty of Actuaries - Financial Mathematics",
                    totalQuestions = 15,
                    passingScore = 60,
                    duration = 3 * 60
                ),
                ExamEntity(
                    name = "IFOA Exam CM1 (Actuarial Mathematics)",
                    organizationCode = "IFOA",
                    description = "Institute and Faculty of Actuaries - Core Principles",
                    totalQuestions = 15,
                    passingScore = 60,
                    duration = 3 * 60
                ),
                ExamEntity(
                    name = "ASSA Exam 101 (Actuarial Practice)",
                    organizationCode = "ASSA",
                    description = "Actuarial Society of South Africa - Actuarial Practice",
                    totalQuestions = 20,
                    passingScore = 65,
                    duration = 3 * 60
                ),
                ExamEntity(
                    name = "ICAS Exam AF1 (Actuarial Finance)",
                    organizationCode = "ICAS",
                    description = "Institute of Chartered Accountants of Scotland - Actuarial Finance",
                    totalQuestions = 18,
                    passingScore = 60,
                    duration = 3 * 60
                )
            )
            examDao.insertExams(exams)
        }
    }
}
