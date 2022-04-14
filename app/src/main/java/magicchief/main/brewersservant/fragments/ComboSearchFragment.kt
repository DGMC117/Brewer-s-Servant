package magicchief.main.brewersservant.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.view.get
import androidx.core.view.size
import androidx.core.widget.doOnTextChanged
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputLayout
import magicchief.main.brewersservant.DBHelper
import magicchief.main.brewersservant.R

class ComboSearchFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_combo_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val dbHelper = DBHelper(requireContext())

        val cardNamesChipGroup = requireView().findViewById<ChipGroup>(R.id.combo_card_name_chip_group)
        val cardNameAndOrChipGroup = requireView().findViewById<ChipGroup>(R.id.combo_card_name_and_or_chip_group)
        val cardNameTextView = requireView().findViewById<TextInputLayout>(R.id.combo_card_name_text)
        var nameCatalog: MutableList<String> = ArrayList()
        var nameAdapter = ArrayAdapter(requireContext(), R.layout.list_item, nameCatalog)
        (cardNameTextView.editText as? AutoCompleteTextView)?.setAdapter(nameAdapter)
        cardNameTextView.editText?.doOnTextChanged { text, start, before, count ->
            if (text != null && text!!.length > 2) nameCatalog = dbHelper.getCardNameCatalog (text.toString())
            else nameCatalog = ArrayList()
            nameAdapter = ArrayAdapter(requireContext(), R.layout.list_item, nameCatalog)
            (cardNameTextView.editText as? AutoCompleteTextView)?.setAdapter(nameAdapter)
        }
        (cardNameTextView.editText as? AutoCompleteTextView)?.setOnItemClickListener { adapterView, view, i, l ->
            cardNamesChipGroup.visibility = View.VISIBLE
            val row = nameAdapter.getItem(0)
            var notRepeated = true
            var k = 0
            while (notRepeated && k < cardNamesChipGroup.size) {
                val chip = cardNamesChipGroup.get(k) as Chip
                if (row == chip.text.toString()) notRepeated = false
                ++k
            }
            if (notRepeated) {
                val inflater = LayoutInflater.from(requireContext())
                val chipItem = inflater.inflate(R.layout.card_parameter_chip_item, null, false) as Chip
                chipItem.text = row
                chipItem.setOnCloseIconClickListener {
                    cardNamesChipGroup.removeView(it)
                    if (cardNamesChipGroup.size < 1) cardNamesChipGroup.visibility = View.GONE
                }
                cardNamesChipGroup.addView(chipItem)
                chipItem.isChecked = true
            }
            else Toast.makeText(activity, R.string.card_name_repeated, Toast.LENGTH_SHORT).show()
            (cardNameTextView.editText as? AutoCompleteTextView)?.setText("")
        }

        val colorOperatorToggle = requireView().findViewById<MaterialButtonToggleGroup>(R.id.combo_color_operator_toggle_group)
        colorOperatorToggle.check(R.id.combo_color_exactly_button)

        val colorToggleGroup = requireView().findViewById<MaterialButtonToggleGroup>(R.id.combo_color_toggle_group)
        colorToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                if (checkedId == R.id.combo_color_colorless_button) {
                    group.clearChecked()
                    group.check(checkedId)
                } else {
                    group.uncheck(R.id.combo_color_colorless_button)
                }
            }
        }

        val resultTextView = requireView().findViewById<TextInputLayout>(R.id.combo_result)
    }
}