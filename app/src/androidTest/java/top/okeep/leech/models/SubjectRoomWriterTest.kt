package top.okeep.leech.models

import android.database.sqlite.SQLiteConstraintException
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SubjectRoomWriterTest {
    private lateinit var db: AppDatabase
    private lateinit var accessor: SubjectAccessible

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = AppDatabase.getTestDatabase(context)
        accessor = db.subjectAccessor()
    }

    @Test
    @Throws(Exception::class)
    fun insertValidSubject() = runBlocking<Unit> {
        val v1 = Subject.newInstance("My Opposite")

        val newId = accessor.insertOne(v1)
        val actual = accessor.getOneById(v1.id)

        Assert.assertEquals(v1.id, newId)
        Assert.assertNotNull(actual)
        actual?.let {
            Assert.assertEquals(v1.title, it.title)
            Assert.assertNotNull(it.remark)
        }
    }

    @Test(expected = SQLiteConstraintException::class)
    @Throws(Exception::class)
    fun insertSubjectsWithTheSameTitle() = runBlocking<Unit> {
        val theTitle = "The title"
        val v1 = Subject.newInstance(theTitle)
        delay(1L) // for creating different ids
        val v2 = Subject.newInstance(theTitle)

        accessor.insertOne(v1)
        accessor.insertOne(v2)
    }

    @Test(expected = SQLiteConstraintException::class)
    @Throws(Exception::class)
    fun insertSubjectsWithTheSameId() = runBlocking<Unit> {
        val theSameId = 12L
        val v1 = Subject(id = theSameId, title = "First Opposite", remark = "")
        val v2 = Subject(id = theSameId, title = "Second Opposite", remark = "")

        accessor.insertOne(v1)
        accessor.insertOne(v2)
    }

    @Test
    @Throws(Exception::class)
    fun updateExistingSubject() = runBlocking<Unit> {
        val v1 = Subject.newInstance("My Purpose")
        accessor.insertOne(v1)
        val v2 = v1.copy(title = "New Title")

        val updated = accessor.updateOne(v2)
        val actual = accessor.getOneById(v2.id)

        Assert.assertEquals(1, updated)
        Assert.assertNotNull(actual)
        actual?.let {
            Assert.assertEquals(v2.title, it.title)
            Assert.assertEquals(v2.milliStamp, it.milliStamp)
            Assert.assertNotNull(it.remark)
        }
    }

    @Test
    @Throws(Exception::class)
    fun updateNonexistentSubject() = runBlocking<Unit> {
        val v1 = Subject.newInstance("My Purpose")

        val updated = accessor.updateOne(v1)

        Assert.assertEquals(0, updated)
    }

    @Test(expected = SQLiteConstraintException::class)
    @Throws(Exception::class)
    fun updateSubjectWithConflictingTitle() = runBlocking<Unit> {
        val v1 = Subject.newInstance("My Purpose 1")
        accessor.insertOne(v1)
        val v2 = Subject.newInstance("My Purpose 2")
        accessor.insertOne(v2)
        val v3 = v1.copy(title = "My Purpose 2")

        accessor.updateOne(v3)
    }

    @Test
    @Throws(Exception::class)
    fun deleteExistingSubject() = runBlocking<Unit> {
        val v1 = Subject.newInstance("My Purpose 1")
        accessor.insertOne(v1)
        val v2 = Subject.newInstance("My Purpose 2")
        accessor.insertOne(v2)

        val deleted = accessor.deleteOne(v1)
        val nonexistent = accessor.getOneById(v1.id)
        val available = accessor.getAll()

        Assert.assertEquals(1, deleted)
        Assert.assertNull(nonexistent)
        Assert.assertEquals(1, available.size)
    }

    @Test
    @Throws(Exception::class)
    fun deleteSubjectWithRelatedPayments() = runBlocking<Unit> {
        DataPopulation.populate(db)

        var related = accessor.getOneWithPayments(DataPopulation.vp1.id)
        var payments = db.paymentAccessor().getAll()

        Assert.assertNotNull(related)
        Assert.assertNotNull(related?.payments)
        Assert.assertEquals(4, payments.size)

        related?.payments?.let {
            Assert.assertEquals(2, it.size)

            val deleted = accessor.deleteOne(DataPopulation.vp1)

            Assert.assertEquals(1, deleted)

            related = accessor.getOneWithPayments(DataPopulation.vp1.id)
            payments = db.paymentAccessor().getAll()

            Assert.assertNull(related)
            Assert.assertEquals(2, payments.size)
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteNonexistentSubject() = runBlocking<Unit> {
        val v1 = Subject.newInstance("My Opposite 1")

        val deleted = accessor.deleteOne(v1)

        Assert.assertEquals(0, deleted)
    }

    @After
    @Throws(IOException::class)
    fun teardown() {
        db.close()
    }
}