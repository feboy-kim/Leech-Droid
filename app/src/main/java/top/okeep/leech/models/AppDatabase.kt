package top.okeep.leech.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Subject::class, Payment::class],
    views = [PaymentWithSubject::class],
    version = 2, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun subjectAccessor(): SubjectAccessible
    abstract fun paymentAccessor(): PaymentAccessible

    companion object {
        fun getTestDatabase(cx: Context): AppDatabase {
            return Room.inMemoryDatabaseBuilder(cx, AppDatabase::class.java).build()
        }

        fun getWorkDatabase(cx: Context): AppDatabase {
            return Room.databaseBuilder(cx, AppDatabase::class.java, "leech-book.db3").build()
        }
    }
}