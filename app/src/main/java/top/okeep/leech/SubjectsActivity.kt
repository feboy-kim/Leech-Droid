package top.okeep.leech

import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.android.synthetic.main.subjects_activity.*
import top.okeep.leech.models.AppDatabase

class SubjectsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.subjects_activity)
        setSupportActionBar(toolbar)
    }

    class DataViewModel(cx: Application) : AndroidViewModel(cx) {
        val db: AppDatabase by lazy {
            AppDatabase.getWorkDatabase(cx.applicationContext)
        }
        var dataChanged = false
    }
}