package top.okeep.leech.ui.subjects

import android.database.sqlite.SQLiteException
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import top.okeep.leech.models.Subject
import top.okeep.leech.models.SubjectAccessible

class SubjectsViewModel : ViewModel() {
    val entities: MutableLiveData<List<Subject>> by lazy {
        MutableLiveData<List<Subject>>()
    }
    val loadException: MutableLiveData<SQLiteException> by lazy {
        MutableLiveData<SQLiteException>()
    }

    fun loadSubjects(accessor: SubjectAccessible) =
        viewModelScope.launch(context = Dispatchers.Default) {
            try {
                val data = accessor.getAll()
                entities.postValue(data)
            } catch (se: SQLiteException) {
                loadException.postValue(se)
            }
        }
}