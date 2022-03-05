package magicchief.main.brewersservant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = Preferences(applicationContext)
        if (!sharedPref.databaseDownloadDone) {
            intent = Intent (this, CardDownloadActivity::class.java)
            startActivity(intent)
        }
    }
}