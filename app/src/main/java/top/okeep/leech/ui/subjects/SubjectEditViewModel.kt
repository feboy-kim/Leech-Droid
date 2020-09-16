package top.okeep.leech.ui.subjects

import android.database.sqlite.SQLiteException
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import top.okeep.leech.models.Subject
import top.okeep.leech.models.SubjectAccessible

class SubjectEditViewModel : ViewModel() {
    val original: MutableLiveData<Subject> by lazy {
        MutableLiveData<Subject>()
    }
    val loadException: MutableLiveData<SQLiteException> by lazy {
        MutableLiveData<SQLiteException>()
    }
    val subjectTitle: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val subjectEnabled: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val subjectRemark: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val saveException: MutableLiveData<SQLiteException> by lazy {
        MutableLiveData<SQLiteException>()
    }
    val insertSuccess: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val updateSuccess: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val isValidlySaved = MediatorLiveData<Boolean>().apply {
        addSource(insertSuccess) {
            postValue(it)
        }
        addSource(updateSuccess) {
            postValue(it)
        }
    }
    val isValidlyChanged = MediatorLiveData<Boolean>().apply {
        addSource(subjectTitle) {
            postValue(validlyChanged)
        }
        addSource(subjectEnabled) {
            postValue(validlyChanged)
        }
        addSource(subjectRemark) {
            postValue(validlyChanged)
        }
    }

    private val validlyChanged: Boolean
        get() {
            val entity = assembleSubject() ?: return false
            original.value?.let {
                return it != entity
            } ?: return false
        }

    fun loadSubject(accessor: SubjectAccessible, id: Long = 0L): Job {
        return viewModelScope.launch(context = Dispatchers.Default) {
            try {
                val entity = if (id == 0L) {
                    Subject.newInstance()
                } else {
                    accessor.getOneById(id) ?: Subject.newInstance()
                }
                postLoadedValues(entity)
            } catch (se: SQLiteException) {
                loadException.postValue(se)
            }
        }
    }

    private fun postLoadedValues(entity: Subject) {
        original.postValue(entity)
        subjectTitle.postValue(entity.title)
        subjectEnabled.postValue(entity.enabled)
        subjectRemark.postValue(entity.remark)
    }

    private fun assembleSubject(): Subject? {
        val origin = original.value ?: return null
        val myTitle = subjectTitle.value?.also {
            it.trim()
        } ?: return null
        val isEnabled = subjectEnabled.value ?: return null
        val myRemark = subjectRemark.value?.also {
            it.trim()
        } ?: return null
        return if (myTitle.isBlank()) null else origin.copy(
            title = myTitle,
            remark = myRemark,
            enabled = isEnabled
        )
    }

    fun saveSubject(accessor: SubjectAccessible): Job {
        return viewModelScope.launch(context = Dispatchers.Default) {
            val entity = assembleSubject()?.also {
                it.milliStamp = System.currentTimeMillis()
            }
            try {
                if (original.value?.milliStamp == Long.MIN_VALUE) {
                    entity?.let {
                        accessor.insertOne(it)
                        insertSuccess.postValue(true)
                    } ?: insertSuccess.postValue(false)
                } else {
                    entity?.let {
                        val updated = accessor.updateOne(it)
                        updateSuccess.postValue(updated > 0)
                    } ?: updateSuccess.postValue(false)
                }
            } catch (se: SQLiteException) {
                saveException.postValue(se)
            }
        }
    }
}