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
        val cardText = intent.getStringExtra("card_text")
        val manaValueParamsArray = intent.getStringArrayExtra("mana_value_params")
        val powerParamsArray = intent.getStringArrayExtra("power_params")
        val toughnessParamsArray = intent.getStringArrayExtra("toughness_params")
        val loyaltyParamsArray = intent.getStringArrayExtra("loyalty_params")
        val rarityParamsArray = intent.getStringArrayExtra("rarity_params")
        val legalityParamsArray = intent.getStringArrayExtra("legality_params")
        val layoutParamsArray = intent.getStringArrayExtra("layout_params")

        val db = DBHelper(applicationContext)
        val cards = db.getCards(cardName, cardTypesArray, isCardTypesArray, cardTypesAnd, cardText, manaValueParamsArray, powerParamsArray, toughnessParamsArray, loyaltyParamsArray, rarityParamsArray, legalityParamsArray, layoutParamsArray)

        var cardList = findViewById<RecyclerView>(R.id.card_list)
        layoutManager = LinearLayoutManager (this)
        cardList.layoutManager = layoutManager
        adapter = CardListAdapter(cards)
        cardList.adapter = adapter
    }
}