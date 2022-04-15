package magicchief.main.brewersservant.fragments

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.core.view.size
import androidx.core.widget.doOnTextChanged
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import magicchief.main.brewersservant.*
import magicchief.main.brewersservant.dataclass.Card
import magicchief.main.brewersservant.dataclass.Deck

class DecksListFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<DecksListAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_decks_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = DBHelper(requireContext())
        val decks = db.getDecks()

        var decksList = requireView().findViewById<RecyclerView>(R.id.decks_list_recycler)
        decksList.addItemDecoration(GridSpacingItemDecoration(2, 25, true))
        layoutManager = GridLayoutManager(requireContext(), 2)
        decksList.layoutManager = layoutManager
        adapter = DecksListAdapter(decks, requireContext())
        decksList.adapter = adapter

        val addButton = requireView().findViewById<FloatingActionButton>(R.id.add_deck_fab)
        addButton.setOnClickListener {
            val addDeckDialog = LayoutInflater.from(requireContext()).inflate(R.layout.add_deck_dialog, null, false)
            val dialog = MaterialAlertDialogBuilder(requireContext())
                .setView(addDeckDialog)
                .setTitle(R.string.add_deck)
                .show()
            val deckNameText = addDeckDialog.findViewById<TextInputLayout>(R.id.add_deck_name_text)
            val deckFormatText = addDeckDialog.findViewById<TextInputLayout>(R.id.add_deck_format_text)
            val deckCommanderLayout = addDeckDialog.findViewById<ConstraintLayout>(R.id.add_deck_commander_input_layout)
            val deckCommanderNamesChipGroup = addDeckDialog.findViewById<ChipGroup>(R.id.add_deck_name_chip_group)
            val deckCommanderNameText = addDeckDialog.findViewById<TextInputLayout>(R.id.add_deck_commander_name_text)
            val addDeckDialogButton = addDeckDialog.findViewById<MaterialButton>(R.id.add_deck_dialog_button)

            val formatValues = listOf("Standard", "Pioneer", "Modern", "Legacy", "Vintage", "Brawl", "Historic", "Pauper", "Penny", "Commander")
            val formatAdapter = ArrayAdapter(requireContext(), R.layout.list_item, formatValues)
            (deckFormatText.editText as? AutoCompleteTextView)?.setAdapter(formatAdapter)
            (deckFormatText.editText as? AutoCompleteTextView)?.setText(formatValues[0], false)
            (deckFormatText.editText as? AutoCompleteTextView)?.setOnItemClickListener { adapterView, view, i, l ->
                if (formatAdapter.getItem(i) == "Commander") deckCommanderLayout.visibility = View.VISIBLE
                else deckCommanderLayout.visibility = View.GONE
            }

            var commanderNameCatalog: MutableList<String> = ArrayList()
            var commanderNameAdapter = ArrayAdapter(requireContext(), R.layout.list_item, commanderNameCatalog)
            (deckCommanderNameText.editText as? AutoCompleteTextView)?.setAdapter(commanderNameAdapter)
            deckCommanderNameText.editText?.doOnTextChanged { text, start, before, count ->
                if (text != null && text!!.length > 2) commanderNameCatalog = db.getCardNameCatalog (text.toString())
                else commanderNameCatalog = ArrayList()
                commanderNameAdapter = ArrayAdapter(requireContext(), R.layout.list_item, commanderNameCatalog)
                (deckCommanderNameText.editText as? AutoCompleteTextView)?.setAdapter(commanderNameAdapter)
            }
            (deckCommanderNameText.editText as? AutoCompleteTextView)?.setOnItemClickListener { adapterView, view, i, l ->
                deckCommanderNamesChipGroup.visibility = View.VISIBLE
                val row = commanderNameAdapter.getItem(0)
                var notRepeated = true
                var k = 0
                while (notRepeated && k < deckCommanderNamesChipGroup.size) {
                    val chip = deckCommanderNamesChipGroup.get(k) as Chip
                    if (row == chip.text.toString()) notRepeated = false
                    ++k
                }
                if (notRepeated) {
                    val inflater = LayoutInflater.from(requireContext())
                    val chipItem = inflater.inflate(R.layout.card_parameter_chip_item, null, false) as Chip
                    chipItem.text = row
                    chipItem.setOnCloseIconClickListener {
                        deckCommanderNamesChipGroup.removeView(it)
                        if (deckCommanderNamesChipGroup.size < 1) deckCommanderNamesChipGroup.visibility = View.GONE
                    }
                    deckCommanderNamesChipGroup.addView(chipItem)
                    chipItem.isChecked = true
                }
                else Toast.makeText(activity, R.string.card_name_repeated, Toast.LENGTH_SHORT).show()
                (deckCommanderNameText.editText as? AutoCompleteTextView)?.setText("")
            }

            addDeckDialogButton.setOnClickListener {
                if (deckNameText.editText?.text.toString() == "") Toast.makeText(
                    requireContext(),
                    R.string.no_deck_name_set,
                    Toast.LENGTH_SHORT
                ).show()
                else {
                    var commanderCards: MutableList<Card> = ArrayList()
                    deckCommanderNamesChipGroup.forEach { commanderCards.add(db.getCard(db.getCardIdFromName((it as Chip).text.toString()))) }
                    var commanderIds: MutableList<String> = ArrayList()
                    commanderCards.forEach { commanderIds.add(it.id.toString()) }
                    val id = db.addDeck(
                        Deck(null,
                            deckNameText.editText?.text.toString(),
                            deckFormatText.editText?.text.toString(),
                            if (commanderCards.isNullOrEmpty()) null else if (
                                commanderCards[0].image_uris != null &&
                                commanderCards[0].image_uris?.art_crop != null &&
                                commanderCards[0].image_uris?.art_crop?.toString() != "null") commanderCards[0].image_uris?.art_crop.toString() else
                                commanderCards[0].card_faces?.get(0)?.image_uris?.art_crop.toString(),
                            if (commanderCards.isNullOrEmpty()) "C" else getDeckColorIdentity(commanderCards.toTypedArray())),
                        commanderIds.toTypedArray())
                    dialog.dismiss()
                    val action = DecksListFragmentDirections.actionDecksListFragmentToDeckFragment(deckId = id.toInt())
                    view.findNavController().navigate(action)
                }
            }
        }
    }

    fun getDeckColorIdentity (cards: Array<Card>): String {
        if (cards.isEmpty()) return "C"
        var w = false
        var u = false
        var b = false
        var r = false
        var g = false
        cards.forEach {
            if (it.color_identity!!.contains("W")) w = true
            if (it.color_identity!!.contains("U")) u = true
            if (it.color_identity!!.contains("B")) b = true
            if (it.color_identity!!.contains("R")) r = true
            if (it.color_identity!!.contains("G")) g = true
        }
        var result = ""
        result += if(w) if (result.length > 0) ",W" else "W" else ""
        result += if(u) if (result.length > 0) ",U" else "U" else ""
        result += if(b) if (result.length > 0) ",B" else "B" else ""
        result += if(r) if (result.length > 0) ",R" else "R" else ""
        result += if(g) if (result.length > 0) ",G" else "G" else ""
        return result
    }
}