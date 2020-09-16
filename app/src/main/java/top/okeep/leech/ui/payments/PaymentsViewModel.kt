package top.okeep.leech.ui.payments

import android.database.sqlite.SQLiteException
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import top.okeep.leech.models.Payment
import top.okeep.leech.models.PaymentAccessible

class PaymentsViewModel() : ViewModel() {
    val entities: MutableLiveData<List<Payment>> by lazy {
        MutableLiveData<List<Payment>>()
    }
    val loadException: MutableLiveData<SQLiteException> by lazy {
        MutableLiveData<SQLiteException>()
    }

    fun loadPayments(accessor: PaymentAccessible) =
        viewModelScope.launch(context = Dispatchers.Default) {
            try {
                val data = accessor.getAll()
                entities.postValue(data)
            } catch (se: SQLiteException) {
                loadException.postValue(se)
            }
        }
}