package top.okeep.leech.models

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import top.okeep.leech.models.DataPopulation.p1_m1
import top.okeep.leech.models.DataPopulation.p1_m2
import top.okeep.leech.models.DataPopulation.p2_m1
import top.okeep.leech.models.DataPopulation.p2_m2
import top.okeep.leech.models.DataPopulation.vp1
import top.okeep.leech.models.DataPopulation.vp2
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class PaymentRoomReaderTest {
    private lateinit var accessor: PaymentAccessible

    @Before
    fun setUp() {
        accessor = db.paymentAccessor()
    }

    @Test
    @Throws(Exception::class)
    fun loadAllPayments() = runBlocking<Unit> {
        val actual = accessor.getAll()

        Assert.assertEquals(4, actual.size)
        Assert.assertNotNull(actual.first() as? Payment)
        Assert.assertNotNull(actual.last() as? Payment)
        Assert.assertNotNull(actual.firstOrNull {
            it.subjectId == vp1.id
        })
        Assert.assertNotNull(actual.firstOrNull {
            it.subjectId == vp2.id
        })
    }

    @Test
    @Throws(Exception::class)
    fun loadValidPeriodPaymentsWithParents() {
        val actual = accessor.getAllByPeriod(p1_m1.paidStamp, p2_m1.paidStamp)

        Assert.assertEquals(3, actual.size)
        Assert.assertNotNull(actual.first() as? PaymentWithSubject)
        Assert.assertNotNull(actual.last() as? PaymentWithSubject)
        Assert.assertNotNull(actual.firstOrNull {
            it.paymentId == p1_m1.id
        })
        Assert.assertNotNull(actual.firstOrNull {
            it.paymentId == p1_m2.id
        })
        Assert.assertNotNull(actual.firstOrNull {
            it.paymentId == p2_m1.id
        })
    }

    @Test
    @Throws(Exception::class)
    fun loadAllPayoutsWithParents() {
        val actual = accessor.getPayouts()

        Assert.assertEquals(2, actual.size)
        Assert.assertNotNull(actual.first() as? PaymentWithSubject)
        Assert.assertNotNull(actual.last() as? PaymentWithSubject)
        Assert.assertNotNull(actual.firstOrNull {
            it.paymentId == p1_m1.id
        })
        Assert.assertNotNull(actual.firstOrNull {
            it.paymentId == p2_m1.id
        })
    }

    @Test
    @Throws(Exception::class)
    fun loadAllReceiptsWithParents() {
        val actual = accessor.getReceipts()

        Assert.assertEquals(2, actual.size)
        Assert.assertNotNull(actual.first() as? PaymentWithSubject)
        Assert.assertNotNull(actual.last() as? PaymentWithSubject)
        Assert.assertNotNull(actual.firstOrNull {
            it.paymentId == p1_m2.id
        })
        Assert.assertNotNull(actual.firstOrNull {
            it.paymentId == p2_m2.id
        })
    }

    @Test
    @Throws(Exception::class)
    fun loadOnePaymentByValidId() {
        val actual = accessor.getOneById(p2_m2.id)

        Assert.assertNotNull(actual)
        actual?.let {
            Assert.assertEquals(p2_m2.paidStamp, it.paidStamp)
            Assert.assertEquals(p2_m2.payAmount, it.payAmount, 1.0E-9)
        }
    }

    @Test
    @Throws(Exception::class)
    fun loadOnePaymentByInvalidId() {
        val actual = accessor.getOneById(12345L)

        Assert.assertNull(actual)
    }

    companion object {
        private lateinit var db: AppDatabase

        @JvmStatic
        @BeforeClass
        fun beforeAllTests() {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            db = AppDatabase.getTestDatabase(context)
            DataPopulation.populate(db)
        }

        @JvmStatic
        @AfterClass
        @Throws(IOException::class)
        fun afterAllTests() {
            db.close()
        }
    }
}