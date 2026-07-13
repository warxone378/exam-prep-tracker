package com.actuarypro.examtracker.data.database.daos

import androidx.room.*
import com.actuarypro.examtracker.data.database.entities.FlashcardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcard(flashcard: FlashcardEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcards(flashcards: List<FlashcardEntity>)

    @Query("SELECT * FROM flashcards WHERE id = :flashcardId")
    suspend fun getFlashcardById(flashcardId: Int): FlashcardEntity?

    @Query("SELECT * FROM flashcards WHERE examId = :examId ORDER BY nextReviewDate ASC")
    fun getFlashcardsByExam(examId: Int): Flow<List<FlashcardEntity>>

    @Query("""SELECT * FROM flashcards 
        WHERE examId = :examId AND nextReviewDate <= :currentTime
        ORDER BY easinessValue ASC LIMIT :limit""")
    suspend fun getDueFlashcards(examId: Int, currentTime: Long, limit: Int): List<FlashcardEntity>

    @Query("SELECT * FROM flashcards WHERE examId = :examId AND topic = :topic ORDER BY nextReviewDate ASC")
    fun getFlashcardsByTopic(examId: Int, topic: String): Flow<List<FlashcardEntity>>

    @Query("SELECT DISTINCT topic FROM flashcards WHERE examId = :examId ORDER BY topic")
    fun getTopicsByExam(examId: Int): Flow<List<String>>

    @Query("""SELECT COUNT(*) FROM flashcards 
        WHERE examId = :examId AND nextReviewDate <= :currentTime""")
    fun getDueFlashcardCount(examId: Int, currentTime: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM flashcards WHERE examId = :examId")
    fun getFlashcardCountByExam(examId: Int): Flow<Int>

    @Update
    suspend fun updateFlashcard(flashcard: FlashcardEntity)

    @Delete
    suspend fun deleteFlashcard(flashcard: FlashcardEntity)

    @Query("DELETE FROM flashcards WHERE examId = :examId")
    suspend fun deleteFlashcardsByExam(examId: Int)
}
