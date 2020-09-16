package top.okeep.leech.models

import androidx.room.*

@Dao
interface PaymentAccessible {
    @Query("SELECT * FROM payments")
    suspend fun getAll(): List<Payment>

    @Query("SELECT * FROM payment_with_subject WHERE paidStamp BETWEEN :start AND :end")
    fun getAllByPeriod(start: Long, end: Long): List<PaymentWithSubject>

    @Query("SELECT * FROM payment_with_subject WHERE payAmount > 0.0")
    fun getPayouts(): List<PaymentWithSubject>

    @Query("SELECT * FROM payment_with_subject WHERE payAmount < 0.0")
    fun getReceipts(): List<PaymentWithSubject>

    @Query("SELECT * FROM payment_with_subject WHERE paymentId = :pId")
    fun getOneWithPurposeById(pId: Long): PaymentWithSubject?

    @Query("SELECT * FROM payments WHERE id = :pId")
    fun getOneById(pId: Long): Payment?

    @Insert
    fun insertOne(payment: Payment): Long

    @Update
    fun updateOne(payment: Payment): Int

    @Delete
    fun deleteOne(payment: Payment): Int
}