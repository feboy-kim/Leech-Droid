package top.okeep.leech.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "subjects", indices = [Index(value = ["title"], unique = true)])
data class Subject(
    @PrimaryKey val id: Long,
    val title: String,
    val remark: String,
    val enabled: Boolean = true,
) {
    var milliStamp: Long = Long.MIN_VALUE

    companion object {
        fun newInstance(t: String = ""): Subject {
            return Subject(id = System.currentTimeMillis(), title = t, remark = "")
        }
    }
}