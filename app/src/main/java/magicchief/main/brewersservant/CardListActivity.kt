package magicchief.main.brewersservant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CardListActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<CardListAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_list)

        val db = DBHelper(applicationContext)
        val cards = db.getCards()

        var card_list = findViewById<RecyclerView>(R.id.card_list)
        layoutManager = LinearLayoutManager (this)
        card_list.layoutManager = layoutManager
        adapter = CardListAdapter(cards)
        card_list.adapter = adapter
    }
}