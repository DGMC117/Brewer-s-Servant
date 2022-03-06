package magicchief.main.brewersservant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class CardListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_list)

        val db = DBHelper(applicationContext)
        val list= db.getCards()
        if (list.size > 0) {
            list.forEach {
                println(it.name)
            }
        }
        else Toast.makeText(applicationContext, "No cards found", Toast.LENGTH_SHORT).show()
    }
}