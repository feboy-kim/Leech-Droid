package top.okeep.leech.view_models

import android.database.sqlite.SQLiteException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import top.okeep.leech.ui.subjects.SubjectsViewModel

@RunWith(AndroidJUnit4::class)
class SubjectsViewModelTest {
    private val goodDao = SubjectAccessMocker(true)
    private val poorDao = SubjectAccessMocker(false)

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun successfullyFetchSubjects() = runBlocking<Unit> {
        val vm = SubjectsViewModel()

        vm.loadSubjects(goodDao).join()

        Assert.assertNotNull(vm.entities.value)
        vm.entities.value?.let {
            Assert.assertEquals(2, it.size)
            Assert.assertTrue(it.contains(SubjectAccessMocker.v1))
            Assert.assertTrue(it.contains(SubjectAccessMocker.v1))
        }
        Assert.assertNull(vm.loadException.value)
    }

    @Test
    fun failedToFetchSubjects() = runBlocking<Unit> {
        val vm = SubjectsViewModel()

        vm.loadSubjects(poorDao).join()

        Assert.assertNull(vm.entities.value)
        Assert.assertNotNull(vm.loadException.value)
        Assert.assertNotNull(vm.loadException.value as? SQLiteException)
    }
}