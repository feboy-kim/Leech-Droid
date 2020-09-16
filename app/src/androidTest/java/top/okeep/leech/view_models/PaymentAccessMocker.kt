package top.okeep.leech.view_models

import android.database.sqlite.SQLiteException
import top.okeep.leech.models.Payment
import top.okeep.leech.models.PaymentAccessible
import top.okeep.leech.models.PaymentWithSubject
import top.okeep.leech.models.Subject

class PaymentAccessMocker(private val good: Boolean) : PaymentAccessible {
    companion object {
        val vp1 = Subject(
            id = 21L,
            title = "Subject A1",
            enabled = true,
            remark = "Subject A description"
        )
        val vp2 = Subject(
            id = 22L,
            title = "Subject B2",
            enabled = false,
            remark = "Subject B description"
        )
        val p1_m1 = Payment(
            id = 101L,
            subjectId = vp1.id,
            payAmount = 101.1,
            paidStamp = 10101L,
            description = "payment 1 description"
        )
        val p1_m2 = Payment(
            id = 102L,
            subjectId = vp1.id,
            payAmount = -102.1,
            paidStamp = 10102L,
            description = "payment 2 description"
        )
        val p2_m1 = Payment(
            id = 103L,
            subjectId = vp2.id,
            payAmount = 101.1,
            paidStamp = 10201L,
            description = "payment 1 description"
        )
        val p2_m2 = Payment(
            id = 104L,
            subjectId = vp2.id,
            payAmount = -102.1,
            paidStamp = 10202L,
            description = "payment 2 description"
        )
    }

    override suspend fun getAll(): List<Payment> {
        return if (good) listOf(
            p1_m1,
            p1_m2,
            p2_m1,
            p2_m2
        ) else throw SQLiteException("getAll failed")
    }

    override fun getAllByPeriod(start: Long, end: Long): List<PaymentWithSubject> {
        TODO("Not yet implemented")
    }

    override fun getPayouts(): List<PaymentWithSubject> {
        TODO("Not yet implemented")
    }

    override fun getReceipts(): List<PaymentWithSubject> {
        TODO("Not yet implemented")
    }

    override fun getOneWithPurposeById(pId: Long): PaymentWithSubject? {
        TODO("Not yet implemented")
    }

    override fun getOneById(pId: Long): Payment? {
        TODO("Not yet implemented")
    }

    override fun insertOne(payment: Payment): Long {
        TODO("Not yet implemented")
    }

    override fun updateOne(payment: Payment): Int {
        TODO("Not yet implemented")
    }

    override fun deleteOne(payment: Payment): Int {
        TODO("Not yet implemented")
    }
}