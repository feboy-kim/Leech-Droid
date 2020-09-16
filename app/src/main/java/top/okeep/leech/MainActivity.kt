package top.okeep.leech

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import kotlinx.android.synthetic.main.main_activity.*
import top.okeep.leech.models.AppDatabase

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.action_subjects -> {
                val intent = Intent(this, SubjectsActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_payments -> {
                val intent = Intent(this, PaymentsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    class DataViewModel(cx: Application) : AndroidViewModel(cx) {
        val db: AppDatabase by lazy {
            AppDatabase.getWorkDatabase(cx.applicationContext)
        }
    }
}
