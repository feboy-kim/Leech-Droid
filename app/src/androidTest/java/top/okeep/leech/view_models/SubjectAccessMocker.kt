package top.okeep.leech.view_models

import android.database.sqlite.SQLiteException
import top.okeep.leech.models.Subject
import top.okeep.leech.models.SubjectAccessible
import top.okeep.leech.models.SubjectWithPayments

class SubjectAccessMocker(private val good: Boolean) : SubjectAccessible {
    companion object {
        val v1 = Subject(
            id = 21L,
            title = "Subject A",
            enabled = true,
            remark = "Subject A description"
        ).also {
            it.milliStamp = System.currentTimeMillis()
        }
        val v2 = Subject(
            id = 22L,
            title = "Subject B",
            enabled = false,
            remark = "Subject B description"
        ).also {
            it.milliStamp = System.currentTimeMillis()
        }
        val v1_payments = SubjectWithPayments(
            subject = v1,
            payments = listOf()
        )
        val v2_payments = SubjectWithPayments(
            subject = v2,
            payments = listOf()
        )
    }

    override suspend fun getAll(): List<Subject> {
        return if (good) listOf(v1, v2) else throw SQLiteException("getAll failed")
    }

    override suspend fun getOneById(oId: Long): Subject? {
        if (good) {
            return when (oId) {
                v1.id -> v1
                v2.id -> v2
                else -> null
            }
        } else throw SQLiteException("getOneById failed")
    }

    override suspend fun insertOne(subject: Subject): Long {
        if (good) {
            return when (subject) {
                v1 -> v1.id
                v2 -> v2.id
                else -> 0
            }
        } else throw SQLiteException("insertOne failed")
    }

    override suspend fun updateOne(subject: Subject): Int {
        if (good) {
            return when (subject.id) {
                v1.id -> 1
                v2.id -> 1
                else -> 0
            }
        } else throw SQLiteException("updateOne failed")
    }

    override suspend fun deleteOne(subject: Subject): Int {
        if (good) {
            return when (subject.id) {
                v1.id -> 1
                v2.id -> 1
                else -> 0
            }
        } else throw SQLiteException("deleteOne failed")
    }

    override fun getAllWithPayments(): List<SubjectWithPayments> {
        return if (good) listOf(
            v1_payments,
            v2_payments
        ) else throw SQLiteException("getAllWithPayments failed")
    }

    override fun getOneWithPayments(id: Long): SubjectWithPayments? {
        if (good) {
            return when (id) {
                v1.id -> v1_payments
                v2.id -> v2_payments
                else -> null
            }
        } else throw SQLiteException("getOneWithPayments failed")
    }
}