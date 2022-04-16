package magicchief.main.brewersservant.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
import androidx.core.view.size
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import magicchief.main.brewersservant.*
import magicchief.main.brewersservant.dataclass.Card
import magicchief.main.brewersservant.dataclass.Deck

class DeckCardsFragment : Fragment() {

    var deckId = 1

    var cardList: MutableList<Card> = ArrayList()
    lateinit var deck: Deck

    private var commandersLayoutManager: RecyclerView.LayoutManager? = null
    private var commandersAdapter: RecyclerView.Adapter<DeckCardsAdapter.ViewHolder>? = null
    private var planeswalkersLayoutManager: RecyclerView.LayoutManager? = null
    private var planeswalkersAdapter: RecyclerView.Adapter<DeckCardsAdapter.ViewHolder>? = null
    private var creaturesLayoutManager: RecyclerView.LayoutManager? = null
    private var creaturesAdapter: RecyclerView.Adapter<DeckCardsAdapter.ViewHolder>? = null
    private var artifactsLayoutManager: RecyclerView.LayoutManager? = null
    private var artifactsAdapter: RecyclerView.Adapter<DeckCardsAdapter.ViewHolder>? = null
    private var enchantmentsLayoutManager: RecyclerView.LayoutManager? = null
    private var enchantmentsAdapter: RecyclerView.Adapter<DeckCardsAdapter.ViewHolder>? = null
    private var sorceriesLayoutManager: RecyclerView.LayoutManager? = null
    private var sorceriesAdapter: RecyclerView.Adapter<DeckCardsAdapter.ViewHolder>? = null
    private var instantsLayoutManager: RecyclerView.LayoutManager? = null
    private var instantsAdapter: RecyclerView.Adapter<DeckCardsAdapter.ViewHolder>? = null
    private var landsLayoutManager: RecyclerView.LayoutManager? = null
    private var landsAdapter: RecyclerView.Adapter<DeckCardsAdapter.ViewHolder>? = null
    private var otherLayoutManager: RecyclerView.LayoutManager? = null
    private var otherAdapter: RecyclerView.Adapter<DeckCardsAdapter.ViewHolder>? = null
    private var sideLayoutManager: RecyclerView.LayoutManager? = null
    private var sideAdapter: RecyclerView.Adapter<DeckCardsAdapter.ViewHolder>? = null
    private var maybeLayoutManager: RecyclerView.LayoutManager? = null
    private var maybeAdapter: RecyclerView.Adapter<DeckCardsAdapter.ViewHolder>? = null

    private lateinit var commandersRecycler: RecyclerView
    private lateinit var planeswalkersRecycler: RecyclerView
    private lateinit var creaturesRecycler: RecyclerView
    private lateinit var artifactsRecycler: RecyclerView
    private lateinit var enchantmentsRecycler: RecyclerView
    private lateinit var sorceriesRecycler: RecyclerView
    private lateinit var instantsRecycler: RecyclerView
    private lateinit var landsRecycler: RecyclerView
    private lateinit var otherRecycler: RecyclerView
    private lateinit var sideRecycler: RecyclerView
    private lateinit var maybeRecycler: RecyclerView

    private lateinit var commandersLayout: LinearLayout
    private lateinit var planeswalkersLayout: LinearLayout
    private lateinit var creaturesLayout: LinearLayout
    private lateinit var artifactsLayout: LinearLayout
    private lateinit var enchantmentsLayout: LinearLayout
    private lateinit var sorceriesLayout: LinearLayout
    private lateinit var instantsLayout: LinearLayout
    private lateinit var landsLayout: LinearLayout
    private lateinit var otherLayout: LinearLayout
    private lateinit var sideLayout: LinearLayout
    private lateinit var maybeLayout: LinearLayout

    private lateinit var planeswalkersCountTextView: TextView
    private lateinit var creaturesCountTextView: TextView
    private lateinit var artifactsCountTextView: TextView
    private lateinit var enchantmentsCountTextView: TextView
    private lateinit var sorceriesCountTextView: TextView
    private lateinit var instantsCountTextView: TextView
    private lateinit var landsCountTextView: TextView
    private lateinit var otherCountTextView: TextView
    private lateinit var sideCountTextView: TextView
    private lateinit var maybeCountTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            deckId = it.getInt("deckId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_deck_cards, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dbHelper = DBHelper(requireContext())

        deck = dbHelper.getDeck(deckId)

        commandersLayoutManager = LinearLayoutManager (requireContext())
        planeswalkersLayoutManager = LinearLayoutManager (requireContext())
        creaturesLayoutManager = LinearLayoutManager (requireContext())
        artifactsLayoutManager = LinearLayoutManager (requireContext())
        enchantmentsLayoutManager = LinearLayoutManager (requireContext())
        sorceriesLayoutManager = LinearLayoutManager (requireContext())
        instantsLayoutManager = LinearLayoutManager (requireContext())
        landsLayoutManager = LinearLayoutManager (requireContext())
        otherLayoutManager = LinearLayoutManager (requireContext())
        sideLayoutManager = LinearLayoutManager (requireContext())
        maybeLayoutManager = LinearLayoutManager (requireContext())

        val addCardTextInput = requireView().findViewById<TextInputLayout>(R.id.deck_card_tab_card_name_text)
        val boardButton = requireView().findViewById<MaterialButton>(R.id.deck_card_tab_board_button)
        commandersLayout = requireView().findViewById(R.id.deck_card_tab_commanders_layout)
        val commandersAddButton = requireView().findViewById<MaterialButton>(R.id.deck_card_tab_commanders_add_button)
        commandersRecycler = requireView().findViewById(R.id.deck_card_tab_commanders_recycler)
        planeswalkersLayout = requireView().findViewById(R.id.deck_card_tab_planeswalkers_layout)
        planeswalkersCountTextView = requireView().findViewById(R.id.deck_card_tab_planeswalkers_count_text)
        planeswalkersRecycler = requireView().findViewById(R.id.deck_card_tab_planeswalkers_recycler)
        creaturesLayout = requireView().findViewById(R.id.deck_card_tab_creatures_layout)
        creaturesCountTextView = requireView().findViewById(R.id.deck_card_tab_creatures_count_text)
        creaturesRecycler = requireView().findViewById(R.id.deck_card_tab_creatures_recycler)
        artifactsLayout = requireView().findViewById(R.id.deck_card_tab_artifacts_layout)
        artifactsCountTextView = requireView().findViewById(R.id.deck_card_tab_artifacts_count_text)
        artifactsRecycler = requireView().findViewById(R.id.deck_card_tab_artifacts_recycler)
        enchantmentsLayout = requireView().findViewById(R.id.deck_card_tab_enchantments_layout)
        enchantmentsCountTextView = requireView().findViewById(R.id.deck_card_tab_enchantments_count_text)
        enchantmentsRecycler = requireView().findViewById(R.id.deck_card_tab_enchantments_recycler)
        sorceriesLayout = requireView().findViewById(R.id.deck_card_tab_sorceries_layout)
        sorceriesCountTextView = requireView().findViewById(R.id.deck_card_tab_sorceries_count_text)
        sorceriesRecycler = requireView().findViewById(R.id.deck_card_tab_sorceries_recycler)
        instantsLayout = requireView().findViewById(R.id.deck_card_tab_instants_layout)
        instantsCountTextView = requireView().findViewById(R.id.deck_card_tab_instants_count_text)
        instantsRecycler = requireView().findViewById(R.id.deck_card_tab_instants_recycler)
        landsLayout = requireView().findViewById(R.id.deck_card_tab_lands_layout)
        landsCountTextView = requireView().findViewById(R.id.deck_card_tab_lands_count_text)
        landsRecycler = requireView().findViewById(R.id.deck_card_tab_lands_recycler)
        otherLayout = requireView().findViewById(R.id.deck_card_tab_other_layout)
        otherCountTextView = requireView().findViewById(R.id.deck_card_tab_other_count_text)
        otherRecycler = requireView().findViewById(R.id.deck_card_tab_other_recycler)
        sideLayout = requireView().findViewById(R.id.deck_card_tab_side_layout)
        sideCountTextView = requireView().findViewById(R.id.deck_card_tab_side_count_text)
        sideRecycler = requireView().findViewById(R.id.deck_card_tab_side_recycler)
        maybeLayout = requireView().findViewById(R.id.deck_card_tab_maybe_layout)
        maybeCountTextView = requireView().findViewById(R.id.deck_card_tab_maybe_count_text)
        maybeRecycler = requireView().findViewById(R.id.deck_card_tab_maybe_recycler)

        println(deck.format)
        if (deck.format == getString(R.string.commander)) commandersLayout.visibility = View.VISIBLE

        var nameCatalog: MutableList<String> = ArrayList()
        var nameAdapter = ArrayAdapter(requireContext(), R.layout.list_item, nameCatalog)
        (addCardTextInput.editText as? AutoCompleteTextView)?.setAdapter(nameAdapter)
        addCardTextInput.editText?.doOnTextChanged { text, start, before, count ->
            if (text != null && text!!.length > 2) nameCatalog = dbHelper.getCardNameCatalog (text.toString())
            else nameCatalog = ArrayList()
            nameAdapter = ArrayAdapter(requireContext(), R.layout.list_item, nameCatalog)
            (addCardTextInput.editText as? AutoCompleteTextView)?.setAdapter(nameAdapter)
        }
        (addCardTextInput.editText as? AutoCompleteTextView)?.setOnItemClickListener { adapterView, view, i, l ->
            dbHelper.addCardInDeck(deckId, dbHelper.getCardIdFromName((view as MaterialTextView).text.toString()), 1, boardButton.text.toString().lowercase())
            (addCardTextInput.editText as? AutoCompleteTextView)?.setText("")
            updateLists(dbHelper)
        }

        boardButton.setOnClickListener {
            if ((it as MaterialButton).text == getString(R.string.main)) it.text = getString(R.string.side)
            else if (it.text == getString(R.string.side)) it.text = getString(R.string.maybe)
            else it.text = getString(R.string.main)
        }

        commandersAddButton.setOnClickListener { boardButton.text = getString(R.string.commander) }

        commandersRecycler.layoutManager = commandersLayoutManager
        planeswalkersRecycler.layoutManager = planeswalkersLayoutManager
        creaturesRecycler.layoutManager = creaturesLayoutManager
        artifactsRecycler.layoutManager = artifactsLayoutManager
        enchantmentsRecycler.layoutManager = enchantmentsLayoutManager
        sorceriesRecycler.layoutManager = sorceriesLayoutManager
        instantsRecycler.layoutManager = instantsLayoutManager
        landsRecycler.layoutManager = landsLayoutManager
        otherRecycler.layoutManager = otherLayoutManager
        sideRecycler.layoutManager = sideLayoutManager
        maybeRecycler.layoutManager = maybeLayoutManager

        updateLists(dbHelper)
    }

    fun updateLists(dbHelper: DBHelper) {
        val commanders: MutableList<Card> = ArrayList()
        val planeswalkers: MutableList<Card> = ArrayList()
        val creatures: MutableList<Card> = ArrayList()
        val artifacts: MutableList<Card> = ArrayList()
        val enchantments: MutableList<Card> = ArrayList()
        val sorceries: MutableList<Card> = ArrayList()
        val instants: MutableList<Card> = ArrayList()
        val lands: MutableList<Card> = ArrayList()
        val other: MutableList<Card> = ArrayList()
        val side: MutableList<Card> = ArrayList()
        val maybe: MutableList<Card> = ArrayList()
        cardList = dbHelper.getDeckCards(deckId)

        var w = false
        var u = false
        var b = false
        var r = false
        var g = false

        var planeswalkersCount = 0
        var creaturesCount = 0
        var artifactsCount = 0
        var enchantmentsCount = 0
        var sorceriesCount = 0
        var instantsCount = 0
        var landsCount = 0
        var otherCount = 0
        var sideCount = 0
        var maybeCount = 0

        cardList.forEach {
            if (it.color_identity!!.contains("W")) w = true
            if (it.color_identity!!.contains("U")) u = true
            if (it.color_identity!!.contains("B")) b = true
            if (it.color_identity!!.contains("R")) r = true
            if (it.color_identity!!.contains("G")) g = true
            when (dbHelper.getDeckCardBoard(deckId, it.id.toString())) {
                "commander" -> commanders.add(it)
                "side" -> {
                    side.add(it)
                    sideCount += dbHelper.getAmountInDeck(deckId, it.id.toString())
                }
                "maybe" -> {
                    maybe.add(it)
                    maybeCount += dbHelper.getAmountInDeck(deckId, it.id.toString())
                }
                "main" -> {
                    if (it.type_line!!.contains("Planeswalker", false)) {
                        planeswalkers.add(it)
                        planeswalkersCount += dbHelper.getAmountInDeck(deckId, it.id.toString())
                    }
                    else if (it.type_line!!.contains("Creature", false)) {
                        creatures.add(it)
                        creaturesCount += dbHelper.getAmountInDeck(deckId, it.id.toString())
                    }
                    else if (it.type_line!!.contains("Artifact", false)) {
                        artifacts.add(it)
                        artifactsCount += dbHelper.getAmountInDeck(deckId, it.id.toString())
                    }
                    else if (it.type_line!!.contains("Enchantment", false)) {
                        enchantments.add(it)
                        enchantmentsCount += dbHelper.getAmountInDeck(deckId, it.id.toString())
                    }
                    else if (it.type_line!!.contains("Sorcery", false)) {
                        sorceries.add(it)
                        sorceriesCount += dbHelper.getAmountInDeck(deckId, it.id.toString())
                    }
                    else if (it.type_line!!.contains("Instant", false)) {
                        instants.add(it)
                        instantsCount += dbHelper.getAmountInDeck(deckId, it.id.toString())
                    }
                    else if (it.type_line!!.contains("Land", false)) {
                        lands.add(it)
                        landsCount += dbHelper.getAmountInDeck(deckId, it.id.toString())
                    }
                    else {
                        other.add(it)
                        otherCount += dbHelper.getAmountInDeck(deckId, it.id.toString())
                    }
                }
            }
        }
        var identity = ""
        identity += if(w) if (identity.length > 0) ",W" else "W" else ""
        identity += if(u) if (identity.length > 0) ",U" else "U" else ""
        identity += if(b) if (identity.length > 0) ",B" else "B" else ""
        identity += if(r) if (identity.length > 0) ",R" else "R" else ""
        identity += if(g) if (identity.length > 0) ",G" else "G" else ""
        if (identity == "") identity = "C"
        dbHelper.updateColorIdentityInDeck(deckId, identity)
        if (!commanders.isNullOrEmpty()) {
            commandersAdapter = DeckCardsAdapter(deckId, commanders, requireContext())
            val listener = object : OnItemsClickListener {
                override fun onItemClick() {
                    updateLists(dbHelper)
                }
            }
            (commandersAdapter as DeckCardsAdapter).setOnItemClickListener(listener)
            commandersRecycler.adapter = commandersAdapter
        }
        if (!planeswalkers.isNullOrEmpty()) {
            planeswalkersAdapter = DeckCardsAdapter(deckId, planeswalkers, requireContext())
            val listener = object : OnItemsClickListener {
                override fun onItemClick() {
                    updateLists(dbHelper)
                }
            }
            (planeswalkersAdapter as DeckCardsAdapter).setOnItemClickListener(listener)
            planeswalkersRecycler.adapter = planeswalkersAdapter
            planeswalkersLayout.visibility = View.VISIBLE
            planeswalkersCountTextView.text = planeswalkersCount.toString()
        }
        else planeswalkersLayout.visibility = View.GONE
        if (!creatures.isNullOrEmpty()) {
            creaturesAdapter = DeckCardsAdapter(deckId, creatures, requireContext())
            val listener = object : OnItemsClickListener {
                override fun onItemClick() {
                    updateLists(dbHelper)
                }
            }
            (creaturesAdapter as DeckCardsAdapter).setOnItemClickListener(listener)
            creaturesRecycler.adapter = creaturesAdapter
            creaturesLayout.visibility = View.VISIBLE
            creaturesCountTextView.text = creaturesCount.toString()
        }
        else creaturesLayout.visibility = View.GONE
        if (!artifacts.isNullOrEmpty()) {
            artifactsAdapter = DeckCardsAdapter(deckId, artifacts, requireContext())
            val listener = object : OnItemsClickListener {
                override fun onItemClick() {
                    updateLists(dbHelper)
                }
            }
            (artifactsAdapter as DeckCardsAdapter).setOnItemClickListener(listener)
            artifactsRecycler.adapter = artifactsAdapter
            artifactsLayout.visibility = View.VISIBLE
            artifactsCountTextView.text = artifactsCount.toString()
        }
        else artifactsLayout.visibility = View.GONE
        if (!enchantments.isNullOrEmpty()) {
            enchantmentsAdapter = DeckCardsAdapter(deckId, enchantments, requireContext())
            val listener = object : OnItemsClickListener {
                override fun onItemClick() {
                    updateLists(dbHelper)
                }
            }
            (enchantmentsAdapter as DeckCardsAdapter).setOnItemClickListener(listener)
            enchantmentsRecycler.adapter = enchantmentsAdapter
            enchantmentsLayout.visibility = View.VISIBLE
            enchantmentsCountTextView.text = enchantmentsCount.toString()
        }
        else enchantmentsLayout.visibility = View.GONE
        if (!sorceries.isNullOrEmpty()) {
            sorceriesAdapter = DeckCardsAdapter(deckId, sorceries, requireContext())
            val listener = object : OnItemsClickListener {
                override fun onItemClick() {
                    updateLists(dbHelper)
                }
            }
            (sorceriesAdapter as DeckCardsAdapter).setOnItemClickListener(listener)
            sorceriesRecycler.adapter = sorceriesAdapter
            sorceriesLayout.visibility = View.VISIBLE
            sorceriesCountTextView.text = sorceriesCount.toString()
        }
        else sorceriesLayout.visibility = View.GONE
        if (!instants.isNullOrEmpty()) {
            instantsAdapter = DeckCardsAdapter(deckId, instants, requireContext())
            val listener = object : OnItemsClickListener {
                override fun onItemClick() {
                    updateLists(dbHelper)
                }
            }
            (instantsAdapter as DeckCardsAdapter).setOnItemClickListener(listener)
            instantsRecycler.adapter = instantsAdapter
            instantsLayout.visibility = View.VISIBLE
            instantsCountTextView.text = instantsCount.toString()
        }
        else instantsLayout.visibility = View.GONE
        if (!lands.isNullOrEmpty()) {
            landsAdapter = DeckCardsAdapter(deckId, lands, requireContext())
            val listener = object : OnItemsClickListener {
                override fun onItemClick() {
                    updateLists(dbHelper)
                }
            }
            (landsAdapter as DeckCardsAdapter).setOnItemClickListener(listener)
            landsRecycler.adapter = landsAdapter
            landsLayout.visibility = View.VISIBLE
            landsCountTextView.text = landsCount.toString()
        }
        else landsLayout.visibility = View.GONE
        if (!other.isNullOrEmpty()) {
            otherAdapter = DeckCardsAdapter(deckId, other, requireContext())
            val listener = object : OnItemsClickListener {
                override fun onItemClick() {
                    updateLists(dbHelper)
                }
            }
            (otherAdapter as DeckCardsAdapter).setOnItemClickListener(listener)
            otherRecycler.adapter = otherAdapter
            otherLayout.visibility = View.VISIBLE
            otherCountTextView.text = otherCount.toString()
        }
        else otherLayout.visibility = View.GONE
        if (!side.isNullOrEmpty()) {
            sideAdapter = DeckCardsAdapter(deckId, side, requireContext())
            val listener = object : OnItemsClickListener {
                override fun onItemClick() {
                    updateLists(dbHelper)
                }
            }
            (sideAdapter as DeckCardsAdapter).setOnItemClickListener(listener)
            sideRecycler.adapter = sideAdapter
            sideLayout.visibility = View.VISIBLE
            sideCountTextView.text = sideCount.toString()
        }
        else sideLayout.visibility = View.GONE
        if (!maybe.isNullOrEmpty()) {
            maybeAdapter = DeckCardsAdapter(deckId, maybe, requireContext())
            val listener = object : OnItemsClickListener {
                override fun onItemClick() {
                    updateLists(dbHelper)
                }
            }
            (maybeAdapter as DeckCardsAdapter).setOnItemClickListener(listener)
            maybeRecycler.adapter = maybeAdapter
            maybeLayout.visibility = View.VISIBLE
            maybeCountTextView.text = maybeCount.toString()
        }
        else maybeLayout.visibility = View.GONE
    }
}