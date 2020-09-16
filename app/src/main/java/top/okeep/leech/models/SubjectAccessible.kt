package top.okeep.leech.models

import androidx.room.*

@Dao
interface SubjectAccessible {
    @Query("SELECT * FROM subjects")
    suspend fun getAll(): List<Subject>

    @Query("SELECT * FROM subjects WHERE id = :oId")
    suspend fun getOneById(oId: Long): Subject?

    @Insert
    suspend fun insertOne(subject: Subject): Long

    @Update
    suspend fun updateOne(subject: Subject): Int

    @Delete
    suspend fun deleteOne(subject: Subject): Int

    @Transaction
    @Query("SELECT * FROM subjects")
    fun getAllWithPayments(): List<SubjectWithPayments>

    @Transaction
    @Query("SELECT * FROM subjects WHERE id = :id")
    fun getOneWithPayments(id: Long): SubjectWithPayments?
}