package top.okeep.leech.models

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import top.okeep.leech.models.DataPopulation.NONEXISTENT
import top.okeep.leech.models.DataPopulation.p1_m1
import top.okeep.leech.models.DataPopulation.p1_m2
import top.okeep.leech.models.DataPopulation.vp1
import top.okeep.leech.models.DataPopulation.vp2
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SubjectRoomReaderTest {
    private lateinit var accessor: SubjectAccessible

    @Before
    fun setUp() {
        accessor = db.subjectAccessor()
    }

    @Test
    @Throws(Exception::class)
    fun loadAllSubjects() = runBlocking {
        val actual = accessor.getAll()

        Assert.assertEquals(2, actual.size)
        Assert.assertTrue(actual.contains(vp1))
        Assert.assertTrue(actual.contains(vp2))
    }

    @Test
    @Throws(Exception::class)
    fun loadNonexistentSubject() = runBlocking<Unit> {
        val actual = accessor.getOneById(NONEXISTENT)

        Assert.assertNull(actual)
    }

    @Test
    @Throws(Exception::class)
    fun loadOneSubject() = runBlocking {
        val actual = accessor.getOneById(vp1.id)

        Assert.assertNotNull(actual)
        Assert.assertNotNull(actual as? Subject)
        Assert.assertTrue(actual?.title == vp1.title)
    }

    @Test
    @Throws(Exception::class)
    fun loadAllSubjectsWithPayments() {
        val actual = accessor.getAllWithPayments()
        val parent = actual.firstOrNull {
            it.subject == vp1
        }

        Assert.assertEquals(2, actual.size)
        Assert.assertNotNull(parent)
        Assert.assertNotNull(parent?.payments)
        parent?.payments?.let {
            Assert.assertEquals(2, it.size)
            Assert.assertTrue(it.contains(p1_m1))
            Assert.assertTrue(it.contains(p1_m2))
        }
    }

    @Test
    @Throws(Exception::class)
    fun loadOneSubjectWithPayments() {
        val related: SubjectWithPayments? = accessor.getOneWithPayments(vp1.id)

        Assert.assertNotNull(related)
        Assert.assertNotNull(related?.payments)
        related?.let {
            Assert.assertEquals(vp1, it.subject)
            Assert.assertEquals(2, it.payments.size)
            Assert.assertTrue(it.payments.contains(p1_m1))
            Assert.assertTrue(it.payments.contains(p1_m2))
        }
    }

    @Test
    @Throws(Exception::class)
    fun loadInvalidSubjectWithPayments() {
        val related: SubjectWithPayments? = accessor.getOneWithPayments(NONEXISTENT)

        Assert.assertNull(related)
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