package top.okeep.leech.view_models

import android.database.sqlite.SQLiteException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import top.okeep.leech.ui.payments.PaymentsViewModel

@RunWith(AndroidJUnit4::class)
class PaymentsViewModelTest {
    private val goodDao = PaymentAccessMocker(true)
    private val poorDao = PaymentAccessMocker(false)

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun successfullyFetchPayments() = runBlocking<Unit> {
        val vm = PaymentsViewModel()

        vm.loadPayments(goodDao).join()

        Assert.assertNotNull(vm.entities.value)
        vm.entities.value?.let {
            Assert.assertEquals(4, it.size)
            Assert.assertTrue(it.contains(PaymentAccessMocker.p1_m1))
            Assert.assertTrue(it.contains(PaymentAccessMocker.p1_m2))
            Assert.assertTrue(it.contains(PaymentAccessMocker.p2_m1))
            Assert.assertTrue(it.contains(PaymentAccessMocker.p2_m2))
        }
        Assert.assertNull(vm.loadException.value)
    }

    @Test
    fun failedToFetchPayments() = runBlocking<Unit> {
        val vm = PaymentsViewModel()

        vm.loadPayments(poorDao).join()

        Assert.assertNull(vm.entities.value)
        Assert.assertNotNull(vm.loadException.value)
        Assert.assertNotNull(vm.loadException.value as? SQLiteException)
    }}