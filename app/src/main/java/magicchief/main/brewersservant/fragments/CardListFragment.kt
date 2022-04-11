package magicchief.main.brewersservant.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import magicchief.main.brewersservant.CardListAdapter
import magicchief.main.brewersservant.DBHelper
import magicchief.main.brewersservant.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CardListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CardListFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<CardListAdapter.ViewHolder>? = null/*
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null*/

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
        super.onCreate(savedInstanceState)/*
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }*/
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

        val db = DBHelper(requireContext())
        val cards = db.getCards(cardName, cardTypesArray, isCardTypesArray, cardTypesAnd, cardText, manaValueParamsArray, powerParamsArray, toughnessParamsArray, loyaltyParamsArray, rarityParamsArray, legalityParamsArray, layoutParamsArray,
            manaCost, cardColor, colorOperator, cardColorIdentity, cardProducedMana, cardFlavorText, priceCoin, priceOperator, priceValue, cardSet, cardArtist, similarToCardName)

        var cardList = requireView().findViewById<RecyclerView>(R.id.card_list)
        val divider = DividerItemDecoration (requireContext(), DividerItemDecoration.VERTICAL)
        divider.setDrawable(getDrawable(requireContext(), R.drawable.divider)!!)
        cardList.addItemDecoration(divider)
        layoutManager = LinearLayoutManager (requireContext())
        cardList.layoutManager = layoutManager
        adapter = CardListAdapter(cards, requireContext())
        cardList.adapter = adapter
    }
/*
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CardListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CardListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }*/
}