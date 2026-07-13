package com.actuarypro.examtracker.di

import android.content.Context
import androidx.room.Room
import com.actuarypro.examtracker.data.database.AppDatabase
import com.actuarypro.examtracker.data.repositories.ExamRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun providesApplicationScope(): CoroutineScope =
        CoroutineScope(SupervisorJob())

    @Singleton
    @Provides
    fun providesAppDatabase(
        @ApplicationContext context: Context,
        scope: CoroutineScope
    ): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "exam_prep_database"
        )
            .setJournalMode(androidx.room.RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING)
            .fallbackToDestructiveMigration()
            .addCallback(AppDatabase.DatabaseCallback(scope))
            .build()

    @Singleton
    @Provides
    fun providesExamRepository(database: AppDatabase): ExamRepository =
        ExamRepository(
            examDao = database.examDao(),
            questionDao = database.questionDao(),
            userAnswerDao = database.userAnswerDao(),
            studySessionDao = database.studySessionDao(),
            userProgressDao = database.userProgressDao(),
            flashcardDao = database.flashcardDao()
        )
}
