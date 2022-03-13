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

        val cardName = intent.getStringExtra("card_name")
        val cardTypesArray = intent.getStringArrayExtra("card_types")
        val isCardTypesArray = intent.getBooleanArrayExtra("is_card_types")
        val cardTypesAnd = intent.getBooleanExtra("card_type_and", true)

        val db = DBHelper(applicationContext)
        val cards = db.getCards(cardName, cardTypesArray, isCardTypesArray, cardTypesAnd)

        var card_list = findViewById<RecyclerView>(R.id.card_list)
        layoutManager = LinearLayoutManager (this)
        card_list.layoutManager = layoutManager
        adapter = CardListAdapter(cards)
        card_list.adapter = adapter
    }
}