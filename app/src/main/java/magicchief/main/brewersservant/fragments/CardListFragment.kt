package magicchief.main.brewersservant.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import magicchief.main.brewersservant.CardListAdapter
import magicchief.main.brewersservant.DBHelper
import magicchief.main.brewersservant.R

class CardListFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<CardListAdapter.ViewHolder>? = null

    var cardName: String? = null
    var cardTypesArray: Array<String>? = null
    var isCardTypesArray: BooleanArray? = null
    var cardTypesAnd: Boolean = true
    var cardText: String? = null
    var manaValueParamsArray: Array<String>? = null
    var powerParamsArray: Array<String>? = null
    var toughnessParamsArray: Array<String>? = null
    var loyaltyParamsArray: Array<String>? = null
    var rarityParamsArray: Array<String>? = null
    var legalityParamsArray: Array<String>? = null
    var layoutParamsArray: Array<String>? = null
    var manaCost: String? = null
    var cardColor: String? = null
    var colorOperator: String? = null
    var cardColorIdentity: String? = null
    var cardProducedMana: String? = null
    var cardFlavorText: String? = null
    var priceCoin: String? = null
    var priceOperator: String? = null
    var priceValue: String? = null
    var cardSet: String? = null
    var cardArtist: String? = null
    var similarToCardName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cardName = it.getString("cardName").toString()
            cardTypesArray = it.getStringArray("cardTypesArray")
            isCardTypesArray = it.getBooleanArray("isCardTypesArray")
            cardTypesAnd = it.getBoolean("cardTypesAnd")
            cardText = it.getString("cardText").toString()
            manaValueParamsArray = it.getStringArray("manaValueParamsArray")
            powerParamsArray = it.getStringArray("powerParamsArray")
            toughnessParamsArray = it.getStringArray("toughnessParamsArray")
            loyaltyParamsArray = it.getStringArray("loyaltyParamsArray")
            rarityParamsArray = it.getStringArray("rarityParamsArray")
            legalityParamsArray = it.getStringArray("legalityParamsArray")
            layoutParamsArray = it.getStringArray("layoutParamsArray")
            manaCost = it.getString("manaCost").toString()
            cardColor = it.getString("cardColor").toString()
            colorOperator = it.getString("colorOperator").toString()
            cardColorIdentity = it.getString("cardColorIdentity").toString()
            cardProducedMana = it.getString("cardProducedMana").toString()
            cardFlavorText = it.getString("cardFlavorText").toString()
            priceCoin = it.getString("priceCoin").toString()
            priceOperator = it.getString("priceOperator").toString()
            priceValue = it.getString("priceValue").toString()
            cardSet = it.getString("cardSet").toString()
            cardArtist = it.getString("cardArtist").toString()
            similarToCardName = it.getString("similarToCardName").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val noResultstext = requireView().findViewById<TextView>(R.id.card_list_no_results)
        var page = 0

        val db = DBHelper(requireContext())
        val cards = db.getCards(cardName, cardTypesArray, isCardTypesArray, cardTypesAnd, cardText, manaValueParamsArray, powerParamsArray, toughnessParamsArray, loyaltyParamsArray, rarityParamsArray, legalityParamsArray, layoutParamsArray,
            manaCost, cardColor, colorOperator, cardColorIdentity, cardProducedMana, cardFlavorText, priceCoin, priceOperator, priceValue, cardSet, cardArtist, similarToCardName, page)

        var cardList = requireView().findViewById<RecyclerView>(R.id.card_list)
        val divider = DividerItemDecoration (requireContext(), DividerItemDecoration.VERTICAL)
        divider.setDrawable(getDrawable(requireContext(), R.drawable.divider)!!)
        cardList.addItemDecoration(divider)
        layoutManager = LinearLayoutManager (requireContext())
        cardList.layoutManager = layoutManager
        adapter = CardListAdapter(cards, requireContext())
        cardList.adapter = adapter
        if (cards.isEmpty()) noResultstext.visibility = View.VISIBLE

        cardList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (recyclerView.canScrollVertically(-1) && !recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    page++
                    cards.addAll(db.getCards(cardName, cardTypesArray, isCardTypesArray, cardTypesAnd, cardText, manaValueParamsArray, powerParamsArray, toughnessParamsArray, loyaltyParamsArray, rarityParamsArray, legalityParamsArray, layoutParamsArray,
                        manaCost, cardColor, colorOperator, cardColorIdentity, cardProducedMana, cardFlavorText, priceCoin, priceOperator, priceValue, cardSet, cardArtist, similarToCardName, page))
                    adapter = CardListAdapter(cards, requireContext())
                    cardList.adapter?.notifyDataSetChanged()
                }
            }
        })
    }
}