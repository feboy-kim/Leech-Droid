package top.okeep.leech.view_models

import android.database.sqlite.SQLiteException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import top.okeep.leech.models.Subject
import top.okeep.leech.ui.subjects.SubjectEditViewModel

@RunWith(AndroidJUnit4::class)
class SubjectEditViewModelTest {
    private val goodDao = SubjectAccessMocker(true)
    private val poorDao = SubjectAccessMocker(false)

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun successfullyLoadNewSubject() = runBlocking<Unit> {
        val vm = SubjectEditViewModel()

        vm.loadSubject(goodDao).join()

        Assert.assertNotNull(vm.original.value)
        Assert.assertNotNull(vm.subjectTitle.value)
        Assert.assertNotNull(vm.subjectEnabled.value)
        Assert.assertNotNull(vm.subjectRemark.value)
        vm.original.value?.let {
            Assert.assertTrue(it.title.isBlank())
        }
        Assert.assertNull(vm.loadException.value)
    }

    @Test
    fun successfullyLoadExistingSubject() = runBlocking<Unit> {
        val vm = SubjectEditViewModel()

        vm.loadSubject(goodDao, SubjectAccessMocker.v1.id).join()

        Assert.assertNotNull(vm.original.value)
        vm.original.value?.let {
            Assert.assertEquals(SubjectAccessMocker.v1.title, it.title)
            Assert.assertEquals(SubjectAccessMocker.v1.enabled, it.enabled)
        }
        Assert.assertNull(vm.loadException.value)
    }

    @Test
    fun successfullyLoadNonexistentSubjectWithNewOne() = runBlocking<Unit> {
        val vm = SubjectEditViewModel()

        vm.loadSubject(goodDao, 12345L).join()

        Assert.assertNotNull(vm.original.value)
        vm.original.value?.let {
            Assert.assertEquals(Long.MIN_VALUE, it.milliStamp)
            Assert.assertTrue(it.title.isBlank())
        }
        Assert.assertNull(vm.loadException.value)
    }

    @Test
    fun failedToLoadOneSubject() = runBlocking<Unit> {
        val vm = SubjectEditViewModel()

        vm.loadSubject(poorDao, SubjectAccessMocker.v2.id).join()

        Assert.assertNull(vm.original.value)
        Assert.assertNotNull(vm.loadException.value)
        Assert.assertNotNull(vm.loadException.value as? SQLiteException)
    }

    @Test
    fun successfullyInsertSubject() = runBlocking<Unit> {
        val vm = SubjectEditViewModel()
        vm.loadSubject(goodDao).join()
        vm.subjectTitle.value = "New Title"

        vm.saveSubject(goodDao).join()

        Assert.assertNotNull(vm.insertSuccess.value)
        vm.insertSuccess.value?.let {
            Assert.assertTrue(it)
        }
        Assert.assertNull(vm.updateSuccess.value)
        Assert.assertNull(vm.saveException.value)
    }

    @Test
    fun successfullyUpdateSubject() = runBlocking<Unit> {
        val vm = SubjectEditViewModel()
        vm.loadSubject(goodDao, SubjectAccessMocker.v1.id).join()
        vm.subjectTitle.value = "New Title"

        vm.saveSubject(goodDao).join()

        Assert.assertNotNull(vm.updateSuccess.value)
        vm.updateSuccess.value?.let {
            Assert.assertTrue(it)
        }
        Assert.assertNull(vm.insertSuccess.value)
        Assert.assertNull(vm.saveException.value)
    }

    @Test
    fun insertSubjectWithBlankTitle() = runBlocking<Unit> {
        val vm = SubjectEditViewModel()
        vm.loadSubject(goodDao).join()
        vm.subjectTitle.value = " "

        vm.saveSubject(goodDao).join()

        Assert.assertNotNull(vm.insertSuccess.value)
        vm.insertSuccess.value?.let {
            Assert.assertFalse(it)
        }
        Assert.assertNull(vm.updateSuccess.value)
        Assert.assertNull(vm.saveException.value)
    }

    @Test
    fun updateSubjectWithBlankTitle() = runBlocking<Unit> {
        val vm = SubjectEditViewModel()
        vm.loadSubject(goodDao, SubjectAccessMocker.v1.id).join()
        vm.subjectTitle.value = " "

        vm.saveSubject(goodDao).join()

        Assert.assertNotNull(vm.updateSuccess.value)
        vm.updateSuccess.value?.let {
            Assert.assertFalse(it)
        }
        Assert.assertNull(vm.insertSuccess.value)
        Assert.assertNull(vm.saveException.value)
    }

    @Test
    fun failedToInsertSubject() = runBlocking<Unit> {
        val vm = SubjectEditViewModel()
        vm.loadSubject(goodDao).join()
        vm.subjectTitle.value = "New Title"

        vm.saveSubject(poorDao).join()

        Assert.assertNotNull(vm.saveException.value)
        Assert.assertNull(vm.insertSuccess.value)
        Assert.assertNull(vm.updateSuccess.value)
    }

    @Test
    fun failedToUpdateSubject() = runBlocking<Unit> {
        val vm = SubjectEditViewModel()
        vm.loadSubject(goodDao, SubjectAccessMocker.v1.id).join()
        vm.subjectTitle.value = "New Title"

        vm.saveSubject(poorDao).join()

        Assert.assertNotNull(vm.saveException.value)
        Assert.assertNull(vm.insertSuccess.value)
        Assert.assertNull(vm.updateSuccess.value)
    }

}