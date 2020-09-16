package top.okeep.leech.models

import android.database.sqlite.SQLiteConstraintException
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

@RunWith(AndroidJUnit4::class)
class PaymentRoomWriterTest {
    private lateinit var accessor: PaymentAccessible

    @Before
    fun setUp() {
        accessor = db.paymentAccessor()
    }

    @Test
    @Throws(Exception::class)
    fun insertTodayPayment() {
        val today = Calendar.getInstance()
        val v1 = Payment.newInstance(
            pId = vo1.id,
            amount = 123.45,
            payStamp = today.timeInMillis
        )

        val newId = accessor.insertOne(v1)
        val actual = accessor.getOneById(v1.id)

        Assert.assertEquals(v1.id, newId)
        Assert.assertNotNull(actual)
        actual?.let {
            Assert.assertEquals(v1.payAmount, it.payAmount, 1.0E-9)
        }
    }

    @Test(expected = SQLiteConstraintException::class)
    @Throws(Exception::class)
    fun insertOnePaymentWithWrongParentID() {
        val v1 = Payment.newInstance(
            pId = 12345L,
            amount = 123.45,
            payStamp = System.currentTimeMillis()
        )

        accessor.insertOne(v1)
    }

    private fun populatePayments(): Pair<Payment, Payment> {
        val v1 = Payment.newInstance(
            pId = vo1.id,
            amount = 111.11,
            payStamp = System.currentTimeMillis()
        )
        accessor.insertOne(v1)
        Thread.sleep(1)
        val v2 = Payment.newInstance(
            pId = vo2.id,
            amount = 222.22,
            payStamp = System.currentTimeMillis()
        )
        accessor.insertOne(v2)

        return Pair(v1, v2)
    }

    @Test
    @Throws(Exception::class)
    fun updateExistingPayment() {
        val (v1, _) = populatePayments()
        val v3 = v1.copy(payAmount = 333.33)

        val updated = accessor.updateOne(v3)

        Assert.assertEquals(1, updated)

        val newV1 = accessor.getOneById(v1.id)

        Assert.assertNotNull(newV1)
        newV1?.let {
            Assert.assertEquals(v3.payAmount, it.payAmount, 1.0E-9)
        }
    }

    @Test
    @Throws(Exception::class)
    fun updateNonexistentPayment() {
        val v3 = Payment.newInstance(
            pId = vo2.id,
            amount = 123.45,
            payStamp = System.currentTimeMillis()
        )

        val updated = accessor.updateOne(v3)

        Assert.assertEquals(0, updated)
    }

    @Test
    @Throws(Exception::class)
    fun deleteExistingPayment() {
        val (v1, v2) = populatePayments()

        val deleted = accessor.deleteOne(v1)

        Assert.assertEquals(1, deleted)

        val newV1 = accessor.getOneById(v1.id)
        val newV2 = accessor.getOneById(v2.id)

        Assert.assertNull(newV1)
        Assert.assertNotNull(newV2)
    }

    @Test
    @Throws(Exception::class)
    fun deleteNonexistentPayment() {
        val v3 = Payment.newInstance(
            pId = vo2.id,
            amount = 123.45,
            payStamp = System.currentTimeMillis()
        )

        val deleted = accessor.deleteOne(v3)

        Assert.assertEquals(0, deleted)
    }

    @Test(expected = SQLiteConstraintException::class)
    @Throws(Exception::class)
    fun insertConflictingPayments() {
        val theSameId = 12345L
        val v1 = Payment(
            id = theSameId,
            subjectId = vo1.id,
            payAmount = 123.45,
            paidStamp = System.currentTimeMillis(),
            description = ""
        )
        val v2 = Payment(
            id = theSameId,
            subjectId = vo2.id,
            payAmount = 123.45,
            paidStamp = System.currentTimeMillis(),
            description = ""
        )

        accessor.insertOne(v1)
        accessor.insertOne(v2)
    }

    companion object {
        private val vo1 = Subject(
            id = 1L,
            title = "Opposite 1",
            remark = "Opposite 1 description"
        )
        private val vo2 = Subject(
            id = 2L,
            title = "Opposite 2",
            remark = "Opposite 2 description"
        )
        private lateinit var db: AppDatabase

        @JvmStatic
        @BeforeClass
        fun beforeAllTests() = runBlocking<Unit> {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            db = AppDatabase.getTestDatabase(context)
            db.subjectAccessor().insertOne(vo1)
            db.subjectAccessor().insertOne(vo2)
        }

        @JvmStatic
        @AfterClass
        @Throws(IOException::class)
        fun afterAllTests() {
            db.close()
        }
    }
}