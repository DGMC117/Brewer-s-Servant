package magicchief.main.brewersservant

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.alpha
import androidx.core.view.get
import androidx.core.view.size
import androidx.core.widget.doOnTextChanged
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.divider.MaterialDivider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import okhttp3.internal.wait
import org.w3c.dom.Text
import kotlin.system.exitProcess

class CardSearchActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_search)

        val dbHelper = DBHelper (applicationContext)

        val nameTextView = findViewById<TextInputLayout>(R.id.card_name)
        var nameCatalog: MutableList<String> = ArrayList()
        var nameAdapter = ArrayAdapter(applicationContext, R.layout.list_item, nameCatalog)
        (nameTextView.editText as? AutoCompleteTextView)?.setAdapter(nameAdapter)
        nameTextView.editText?.doOnTextChanged { text, start, before, count ->
            if (text != null && text!!.length > 2) nameCatalog = dbHelper.getCardNameCatalog (text.toString())
            else nameCatalog = ArrayList()
            nameAdapter = ArrayAdapter(applicationContext, R.layout.list_item, nameCatalog)
            (nameTextView.editText as? AutoCompleteTextView)?.setAdapter(nameAdapter)
        }

        val cardTypesDivider = findViewById<MaterialDivider>(R.id.card_type_divider)
        val cardTypesChipGroup = findViewById<ChipGroup>(R.id.card_type_chip_group)

        val typeLineTextView = findViewById<TextInputLayout>(R.id.card_type_text)
        val cardTypes = dbHelper.getAllTypeCatalogs()
        val adapter = ArrayAdapter(applicationContext, R.layout.list_item, cardTypes)
        (typeLineTextView.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        (typeLineTextView.editText as? AutoCompleteTextView)?.setOnItemClickListener { adapterView, view, i, l ->
            cardTypesChipGroup.visibility = View.VISIBLE
            cardTypesDivider.visibility = View.VISIBLE
            val row = adapter.getItem(i)
            var notRepeated = true
            var k = 0
            while (notRepeated && k < cardTypesChipGroup.size) {
                val chip = cardTypesChipGroup.get(k) as Chip
                if (row == chip.text.toString()) notRepeated = false
                ++k
            }
            if (notRepeated) {
                val inflater = LayoutInflater.from(this)
                val chipItem = inflater.inflate(R.layout.entry_chip_item, null, false) as Chip
                chipItem.text = row
                chipItem.setOnCloseIconClickListener {
                    cardTypesChipGroup.removeView(it)
                    if (cardTypesChipGroup.size < 1) {
                        cardTypesChipGroup.visibility = View.GONE
                        cardTypesDivider.visibility = View.GONE
                    }
                }
                cardTypesChipGroup.addView(chipItem)
                chipItem.isChecked = true
            }
            else Toast.makeText(applicationContext, R.string.card_type_repeated, Toast.LENGTH_SHORT).show()
            (typeLineTextView.editText as? AutoCompleteTextView)?.setText("")
        }

        val cardTypesAndOrChipGroup = findViewById<ChipGroup>(R.id.type_line_and_or_chip_group)

        val cardTextTextView = findViewById<TextInputLayout>(R.id.card_text_search)

        val textAddSymbolButton = findViewById<Button>(R.id.text_add_mana_symbol_button)
        textAddSymbolButton.setOnClickListener {
            val textSymbolDialog = LayoutInflater.from(this).inflate(R.layout.text_symbol_dialog, null, false)
            val dialog = MaterialAlertDialogBuilder(this)
                .setView(textSymbolDialog)
                .setTitle(R.string.add_mana_symbol)
                .show()
            val costWLayout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_w_layout)
            costWLayout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{W}")
                dialog.dismiss()
            }
            val costULayout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_u_layout)
            costULayout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{U}")
                dialog.dismiss()
            }
            val costBLayout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_b_layout)
            costBLayout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{B}")
                dialog.dismiss()
            }
            val costRLayout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_r_layout)
            costRLayout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{R}")
                dialog.dismiss()
            }
            val costGLayout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_g_layout)
            costGLayout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{G}")
                dialog.dismiss()
            }
            val costWULayout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_wu_layout)
            costWULayout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{W/U}")
                dialog.dismiss()
            }
            val costUBLayout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_ub_layout)
            costUBLayout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{U/B}")
                dialog.dismiss()
            }
            val costBRLayout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_br_layout)
            costBRLayout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{B/R}")
                dialog.dismiss()
            }
            val costRGLayout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_rg_layout)
            costRGLayout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{R/G}")
                dialog.dismiss()
            }
            val costGWLayout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_gw_layout)
            costGWLayout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{G/W}")
                dialog.dismiss()
            }
            val costWBLayout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_wb_layout)
            costWBLayout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{W/B}")
                dialog.dismiss()
            }
            val costURLayout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_ur_layout)
            costURLayout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{U/R}")
                dialog.dismiss()
            }
            val costBGLayout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_bg_layout)
            costBGLayout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{B/G}")
                dialog.dismiss()
            }
            val costRWLayout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_rw_layout)
            costRWLayout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{R/W}")
                dialog.dismiss()
            }
            val costGULayout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_gu_layout)
            costGULayout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{G/U}")
                dialog.dismiss()
            }
            val costWPLayout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_wp_layout)
            costWPLayout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{W/P}")
                dialog.dismiss()
            }
            val costUPLayout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_up_layout)
            costUPLayout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{U/P}")
                dialog.dismiss()
            }
            val costBPLayout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_bp_layout)
            costBPLayout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{B/P}")
                dialog.dismiss()
            }
            val costRPLayout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_rp_layout)
            costRPLayout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{R/P}")
                dialog.dismiss()
            }
            val costGPLayout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_gp_layout)
            costGPLayout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{G/P}")
                dialog.dismiss()
            }
            val costCLayout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_c_layout)
            costCLayout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{C}")
                dialog.dismiss()
            }
            val costSLayout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_s_layout)
            costSLayout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{S}")
                dialog.dismiss()
            }
            val cost0Layout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_0_layout)
            cost0Layout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{0}")
                dialog.dismiss()
            }
            val cost1Layout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_1_layout)
            cost1Layout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{1}")
                dialog.dismiss()
            }
            val cost2Layout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_2_layout)
            cost2Layout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{2}")
                dialog.dismiss()
            }
            val cost3Layout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_3_layout)
            cost3Layout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{3}")
                dialog.dismiss()
            }
            val cost4Layout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_4_layout)
            cost4Layout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{4}")
                dialog.dismiss()
            }
            val cost5Layout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_5_layout)
            cost5Layout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{5}")
                dialog.dismiss()
            }
            val cost6Layout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_6_layout)
            cost6Layout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{6}")
                dialog.dismiss()
            }
            val cost7Layout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_7_layout)
            cost7Layout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{7}")
                dialog.dismiss()
            }
            val cost8Layout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_8_layout)
            cost8Layout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{8}")
                dialog.dismiss()
            }
            val cost9Layout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_9_layout)
            cost9Layout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{9}")
                dialog.dismiss()
            }
            val cost10Layout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_10_layout)
            cost10Layout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{10}")
                dialog.dismiss()
            }
            val cost12Layout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_12_layout)
            cost12Layout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{12}")
                dialog.dismiss()
            }
            val cost20Layout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_20_layout)
            cost20Layout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{20}")
                dialog.dismiss()
            }
            val cost100Layout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_100_layout)
            cost100Layout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{100}")
                dialog.dismiss()
            }
            val costinfinityLayout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_infinity_layout)
            costinfinityLayout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{∞}")
                dialog.dismiss()
            }
            val costXLayout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_x_layout)
            costXLayout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{X}")
                dialog.dismiss()
            }
            val costPLayout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_p_layout)
            costPLayout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{P}")
                dialog.dismiss()
            }
            val cost2WLayout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_2w_layout)
            cost2WLayout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{2/W}")
                dialog.dismiss()
            }
            val costHRLayout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_hr_layout)
            costHRLayout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{HR}")
                dialog.dismiss()
            }
            val costALayout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_a_layout)
            costALayout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{A}")
                dialog.dismiss()
            }
            val costCHAOSLayout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_chaos_layout)
            costCHAOSLayout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{CHAOS}")
                dialog.dismiss()
            }
            val costHALFLayout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_1_2_layout)
            costHALFLayout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{½}")
                dialog.dismiss()
            }
            val costELayout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_e_layout)
            costELayout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{E}")
                dialog.dismiss()
            }
            val costTLayout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_t_layout)
            costTLayout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{T}")
                dialog.dismiss()
            }
            val costQLayout = textSymbolDialog.findViewById<LinearLayout>(R.id.cost_q_layout)
            costQLayout.setOnClickListener {
                cardTextTextView.editText?.setText(cardTextTextView.editText?.text.toString() + "{Q}")
                dialog.dismiss()
            }
        }

        val manaValueLayout = findViewById<LinearLayout>(R.id.mana_value_selected_layout)
        val manaValueInputLayout = findViewById<LinearLayout>(R.id.mana_value_input_layout)
        val manaValueChipGroup = findViewById<ChipGroup>(R.id.mana_value_conditions_chip_group)

        val powerLayout = findViewById<LinearLayout>(R.id.power_selected_layout)
        val powerInputLayout = findViewById<LinearLayout>(R.id.power_input_layout)
        val powerChipGroup = findViewById<ChipGroup>(R.id.power_conditions_chip_group)

        val toughnessLayout = findViewById<LinearLayout>(R.id.toughness_selected_layout)
        val toughnessInputLayout = findViewById<LinearLayout>(R.id.toughness_input_layout)
        val toughnessChipGroup = findViewById<ChipGroup>(R.id.toughness_conditions_chip_group)

        val loyaltyLayout = findViewById<LinearLayout>(R.id.loyalty_selected_layout)
        val loyaltyInputLayout = findViewById<LinearLayout>(R.id.loyalty_input_layout)
        val loyaltyChipGroup = findViewById<ChipGroup>(R.id.loyalty_conditions_chip_group)

        val rarityLayout = findViewById<LinearLayout>(R.id.rarity_selected_layout)
        val rarityInputLayout = findViewById<LinearLayout>(R.id.rarity_input_layout)
        val rarityChipGroup = findViewById<ChipGroup>(R.id.rarity_conditions_chip_group)

        val legalityLayout = findViewById<LinearLayout>(R.id.legality_selected_layout)
        val legalityInputLayout = findViewById<LinearLayout>(R.id.legality_input_layout)
        val legalityChipGroup = findViewById<ChipGroup>(R.id.legality_conditions_chip_group)

        val layoutLayout = findViewById<LinearLayout>(R.id.layout_selected_layout)
        val layoutInputLayout = findViewById<LinearLayout>(R.id.layout_input_layout)
        val layoutChipGroup = findViewById<ChipGroup>(R.id.layout_conditions_chip_group)

        val cardParametersTextView = findViewById<TextInputLayout>(R.id.card_parameter_selector)
        val parametersValues = listOf("Mana Value", "Power", "Toughness", "Loyalty", "Rarity", "Legality", "Layout")
        val parametersAdapter = ArrayAdapter(applicationContext, R.layout.list_item, parametersValues)
        (cardParametersTextView.editText as? AutoCompleteTextView)?.setAdapter(parametersAdapter)
        (cardParametersTextView.editText as? AutoCompleteTextView)?.setText(parametersValues[0], false)
        (cardParametersTextView.editText as? AutoCompleteTextView)?.setOnItemClickListener { adapterView, view, i, l ->
            when (cardParametersTextView.editText?.text.toString())  {
                "Mana Value" -> {
                    manaValueInputLayout.visibility = View.VISIBLE
                    powerInputLayout.visibility = View.GONE
                    toughnessInputLayout.visibility = View.GONE
                    loyaltyInputLayout.visibility = View.GONE
                    rarityInputLayout.visibility = View.GONE
                    legalityInputLayout.visibility = View.GONE
                    layoutInputLayout.visibility = View.GONE
                }
                "Power" -> {
                    powerInputLayout.visibility = View.VISIBLE
                    manaValueInputLayout.visibility = View.GONE
                    toughnessInputLayout.visibility = View.GONE
                    loyaltyInputLayout.visibility = View.GONE
                    rarityInputLayout.visibility = View.GONE
                    legalityInputLayout.visibility = View.GONE
                    layoutInputLayout.visibility = View.GONE
                }
                "Toughness" -> {
                    toughnessInputLayout.visibility = View.VISIBLE
                    powerInputLayout.visibility = View.GONE
                    manaValueInputLayout.visibility = View.GONE
                    loyaltyInputLayout.visibility = View.GONE
                    rarityInputLayout.visibility = View.GONE
                    legalityInputLayout.visibility = View.GONE
                    layoutInputLayout.visibility = View.GONE
                }
                "Loyalty" -> {
                    loyaltyInputLayout.visibility = View.VISIBLE
                    toughnessInputLayout.visibility = View.GONE
                    powerInputLayout.visibility = View.GONE
                    manaValueInputLayout.visibility = View.GONE
                    rarityInputLayout.visibility = View.GONE
                    legalityInputLayout.visibility = View.GONE
                    layoutInputLayout.visibility = View.GONE
                }
                "Rarity" -> {
                    rarityInputLayout.visibility = View.VISIBLE
                    loyaltyInputLayout.visibility = View.GONE
                    toughnessInputLayout.visibility = View.GONE
                    powerInputLayout.visibility = View.GONE
                    manaValueInputLayout.visibility = View.GONE
                    legalityInputLayout.visibility = View.GONE
                    layoutInputLayout.visibility = View.GONE
                }
                "Legality" -> {
                    legalityInputLayout.visibility = View.VISIBLE
                    rarityInputLayout.visibility = View.GONE
                    loyaltyInputLayout.visibility = View.GONE
                    toughnessInputLayout.visibility = View.GONE
                    powerInputLayout.visibility = View.GONE
                    manaValueInputLayout.visibility = View.GONE
                    layoutInputLayout.visibility = View.GONE
                }
                else -> {
                    layoutInputLayout.visibility = View.VISIBLE
                    legalityInputLayout.visibility = View.GONE
                    rarityInputLayout.visibility = View.GONE
                    loyaltyInputLayout.visibility = View.GONE
                    toughnessInputLayout.visibility = View.GONE
                    powerInputLayout.visibility = View.GONE
                    manaValueInputLayout.visibility = View.GONE
                }
            }
        }

        val cardParametersMathRelationTextView = findViewById<TextInputLayout>(R.id.card_parameter_math_relation_selector)
        val parametersMathValues = listOf("==", "!=", "<", ">", "<=", ">=")
        val parametersMathAdapter = ArrayAdapter(applicationContext, R.layout.list_item, parametersMathValues)
        (cardParametersMathRelationTextView.editText as? AutoCompleteTextView)?.setAdapter(parametersMathAdapter)
        (cardParametersMathRelationTextView.editText as? AutoCompleteTextView)?.setText(parametersMathValues[0], false)

        val powerMathRelationTextView = findViewById<TextInputLayout>(R.id.power_math_relation_selector)
        (powerMathRelationTextView.editText as? AutoCompleteTextView)?.setAdapter(parametersMathAdapter)
        (powerMathRelationTextView.editText as? AutoCompleteTextView)?.setText(parametersMathValues[0], false)

        val toughnessMathRelationTextView = findViewById<TextInputLayout>(R.id.toughness_math_relation_selector)
        (toughnessMathRelationTextView.editText as? AutoCompleteTextView)?.setAdapter(parametersMathAdapter)
        (toughnessMathRelationTextView.editText as? AutoCompleteTextView)?.setText(parametersMathValues[0], false)

        val loyaltyMathRelationTextView = findViewById<TextInputLayout>(R.id.loyalty_math_relation_selector)
        (loyaltyMathRelationTextView.editText as? AutoCompleteTextView)?.setAdapter(parametersMathAdapter)
        (loyaltyMathRelationTextView.editText as? AutoCompleteTextView)?.setText(parametersMathValues[0], false)

        val cardManaValueInput = findViewById<TextInputLayout>(R.id.card_mana_value_input)
        val powerValueInput = findViewById<TextInputLayout>(R.id.card_power_input)
        val toughnessValueInput = findViewById<TextInputLayout>(R.id.card_toughness_input)
        val loyaltyValueInput = findViewById<TextInputLayout>(R.id.card_loyalty_input)
        val rarityValueInput = findViewById<TextInputLayout>(R.id.card_rarity_input)
        val rarityValues = listOf("Common", "Uncommon", "Rare", "Mythic")
        val rarityAdapter = ArrayAdapter(applicationContext, R.layout.list_item, rarityValues)
        (rarityValueInput.editText as? AutoCompleteTextView)?.setAdapter(rarityAdapter)
        (rarityValueInput.editText as? AutoCompleteTextView)?.setText(rarityValues[0], false)
        val legalityTypeInput = findViewById<TextInputLayout>(R.id.legality_type_selector)
        val legalityTypeValues = listOf("Legal", "Restricted", "Banned")
        val legalityTypeAdapter = ArrayAdapter(applicationContext, R.layout.list_item, legalityTypeValues)
        (legalityTypeInput.editText as? AutoCompleteTextView)?.setAdapter(legalityTypeAdapter)
        (legalityTypeInput.editText as? AutoCompleteTextView)?.setText(legalityTypeValues[0], false)
        val legalityFormatInput = findViewById<TextInputLayout>(R.id.legality_format_selector)
        val legalityFormatValues = listOf("Standard", "Pioneer", "Modern", "Legacy", "Vintage", "Brawl", "Historic", "Pauper", "Penny", "Commander")
        val legalityFormatAdapter = ArrayAdapter(applicationContext, R.layout.list_item, legalityFormatValues)
        (legalityFormatInput.editText as? AutoCompleteTextView)?.setAdapter(legalityFormatAdapter)
        (legalityFormatInput.editText as? AutoCompleteTextView)?.setText(legalityFormatValues[0], false)
        val layoutValueInput = findViewById<TextInputLayout>(R.id.card_layout_input)
        val layoutValues = listOf("Normal", "Split", "Flip", "Transform", "Modal DFC", "Meld", "Leveler", "Class", "Saga", "Adventure", "Planar", "Scheme", "Vanguard", "Token", "Double Faced Token", "Emblem", "Augment", "Host", "Art Series", "Reversible Card")
        val layoutAdapter = ArrayAdapter(applicationContext, R.layout.list_item, layoutValues)
        (layoutValueInput.editText as? AutoCompleteTextView)?.setAdapter(layoutAdapter)
        (layoutValueInput.editText as? AutoCompleteTextView)?.setText(layoutValues[0], false)

        val addParameterButton = findViewById<Button>(R.id.add_parameter_button)
        addParameterButton.setOnClickListener {
            when (cardParametersTextView.editText?.text.toString())  {
                "Mana Value" ->
                    if (cardManaValueInput.editText?.text.toString() == "") Toast.makeText(applicationContext, R.string.no_mana_value_selected, Toast.LENGTH_SHORT).show()
                    else {
                        manaValueLayout.visibility = View.VISIBLE
                        if (manaValueChipGroup.size != 0) {
                            val inflater = LayoutInflater.from(this)
                            val chipItem = inflater.inflate(R.layout.card_parameter_and_or_chip_item, null, false) as Chip
                            chipItem.text = resources.getString(R.string.and)
                            chipItem.setOnClickListener { chipItem.text = if (chipItem.text == resources.getString(R.string.or)) resources.getString(R.string.and) else resources.getString(R.string.or) }
                            manaValueChipGroup.addView(chipItem)
                            val inflater2 = LayoutInflater.from(this)
                            val chipItem2 = inflater2.inflate(R.layout.card_parameter_chip_item, null, false) as Chip
                            chipItem2.text = cardParametersMathRelationTextView.editText?.text.toString() + " "  + cardManaValueInput.editText?.text.toString()
                            chipItem2.setOnCloseIconClickListener {
                                manaValueChipGroup.removeView(chipItem)
                                manaValueChipGroup.removeView(it)
                                if (manaValueChipGroup.size == 0) manaValueLayout.visibility = View.GONE
                                else if ((manaValueChipGroup.get(0) as Chip).text == resources.getString(R.string.and) || (manaValueChipGroup.get(0) as Chip).text == resources.getString(R.string.or)) manaValueChipGroup.removeView(manaValueChipGroup[0])
                            }
                            manaValueChipGroup.addView(chipItem2)
                        }
                        else {
                            val inflater = LayoutInflater.from(this)
                            val chipItem = inflater.inflate(R.layout.card_parameter_chip_item,null,false) as Chip
                            chipItem.text = cardParametersMathRelationTextView.editText?.text.toString() + " " + cardManaValueInput.editText?.text.toString()
                            chipItem.setOnCloseIconClickListener {
                                manaValueChipGroup.removeView(it)
                                if (manaValueChipGroup.size == 0) manaValueLayout.visibility = View.GONE
                                else if ((manaValueChipGroup.get(0) as Chip).text == resources.getString(R.string.and) || (manaValueChipGroup.get(0) as Chip).text == resources.getString(R.string.or)) manaValueChipGroup.removeView(manaValueChipGroup[0])
                            }
                            manaValueChipGroup.addView(chipItem)
                        }
                        (cardManaValueInput.editText as? TextInputEditText)?.setText("")
                    }
                "Power" ->
                    if (powerValueInput.editText?.text.toString() == "") Toast.makeText(applicationContext, R.string.no_power_selected, Toast.LENGTH_SHORT).show()
                    else {
                        powerLayout.visibility = View.VISIBLE
                        if (powerChipGroup.size != 0) {
                            val inflater = LayoutInflater.from(this)
                            val chipItem = inflater.inflate(R.layout.card_parameter_and_or_chip_item, null, false) as Chip
                            chipItem.text = resources.getString(R.string.and)
                            chipItem.setOnClickListener { chipItem.text = if (chipItem.text == resources.getString(R.string.or)) resources.getString(R.string.and) else resources.getString(R.string.or) }
                            powerChipGroup.addView(chipItem)
                            val inflater2 = LayoutInflater.from(this)
                            val chipItem2 = inflater2.inflate(R.layout.card_parameter_chip_item, null, false) as Chip
                            chipItem2.text = powerMathRelationTextView.editText?.text.toString() + " "  + powerValueInput.editText?.text.toString()
                            chipItem2.setOnCloseIconClickListener {
                                powerChipGroup.removeView(chipItem)
                                powerChipGroup.removeView(it)
                                if (powerChipGroup.size == 0) powerLayout.visibility = View.GONE
                                else if ((powerChipGroup.get(0) as Chip).text == resources.getString(R.string.and) || (powerChipGroup.get(0) as Chip).text == resources.getString(R.string.or)) powerChipGroup.removeView(powerChipGroup[0])
                            }
                            powerChipGroup.addView(chipItem2)
                        }
                        else {
                            val inflater = LayoutInflater.from(this)
                            val chipItem = inflater.inflate(R.layout.card_parameter_chip_item,null,false) as Chip
                            chipItem.text = powerMathRelationTextView.editText?.text.toString() + " " + powerValueInput.editText?.text.toString()
                            chipItem.setOnCloseIconClickListener {
                                powerChipGroup.removeView(it)
                                if (powerChipGroup.size == 0) powerLayout.visibility = View.GONE
                                else if ((powerChipGroup.get(0) as Chip).text == resources.getString(R.string.and) || (powerChipGroup.get(0) as Chip).text == resources.getString(R.string.or)) powerChipGroup.removeView(powerChipGroup[0])
                            }
                            powerChipGroup.addView(chipItem)
                        }
                        (powerValueInput.editText as? TextInputEditText)?.setText("")
                    }
                "Toughness" ->
                    if (toughnessValueInput.editText?.text.toString() == "") Toast.makeText(applicationContext, R.string.no_toughness_selected, Toast.LENGTH_SHORT).show()
                    else {
                        toughnessLayout.visibility = View.VISIBLE
                        if (toughnessChipGroup.size != 0) {
                            val inflater = LayoutInflater.from(this)
                            val chipItem = inflater.inflate(R.layout.card_parameter_and_or_chip_item, null, false) as Chip
                            chipItem.text = resources.getString(R.string.and)
                            chipItem.setOnClickListener { chipItem.text = if (chipItem.text == resources.getString(R.string.or)) resources.getString(R.string.and) else resources.getString(R.string.or) }
                            toughnessChipGroup.addView(chipItem)
                            val inflater2 = LayoutInflater.from(this)
                            val chipItem2 = inflater2.inflate(R.layout.card_parameter_chip_item, null, false) as Chip
                            chipItem2.text = toughnessMathRelationTextView.editText?.text.toString() + " "  + toughnessValueInput.editText?.text.toString()
                            chipItem2.setOnCloseIconClickListener {
                                toughnessChipGroup.removeView(chipItem)
                                toughnessChipGroup.removeView(it)
                                if (toughnessChipGroup.size == 0) toughnessLayout.visibility = View.GONE
                                else if ((toughnessChipGroup.get(0) as Chip).text == resources.getString(R.string.and) || (toughnessChipGroup.get(0) as Chip).text == resources.getString(R.string.or)) toughnessChipGroup.removeView(toughnessChipGroup[0])
                            }
                            toughnessChipGroup.addView(chipItem2)
                        }
                        else {
                            val inflater = LayoutInflater.from(this)
                            val chipItem = inflater.inflate(R.layout.card_parameter_chip_item,null,false) as Chip
                            chipItem.text = toughnessMathRelationTextView.editText?.text.toString() + " " + toughnessValueInput.editText?.text.toString()
                            chipItem.setOnCloseIconClickListener {
                                toughnessChipGroup.removeView(it)
                                if (toughnessChipGroup.size == 0) toughnessLayout.visibility = View.GONE
                                else if ((toughnessChipGroup.get(0) as Chip).text == resources.getString(R.string.and) || (toughnessChipGroup.get(0) as Chip).text == resources.getString(R.string.or)) toughnessChipGroup.removeView(toughnessChipGroup[0])
                            }
                            toughnessChipGroup.addView(chipItem)
                        }
                        (toughnessValueInput.editText as? TextInputEditText)?.setText("")
                    }
                "Loyalty" ->
                    if (loyaltyValueInput.editText?.text.toString() == "") Toast.makeText(applicationContext, R.string.no_loyalty_selected, Toast.LENGTH_SHORT).show()
                    else {
                        loyaltyLayout.visibility = View.VISIBLE
                        if (loyaltyChipGroup.size != 0) {
                            val inflater = LayoutInflater.from(this)
                            val chipItem = inflater.inflate(R.layout.card_parameter_and_or_chip_item, null, false) as Chip
                            chipItem.text = resources.getString(R.string.and)
                            chipItem.setOnClickListener { chipItem.text = if (chipItem.text == resources.getString(R.string.or)) resources.getString(R.string.and) else resources.getString(R.string.or) }
                            loyaltyChipGroup.addView(chipItem)
                            val inflater2 = LayoutInflater.from(this)
                            val chipItem2 = inflater2.inflate(R.layout.card_parameter_chip_item, null, false) as Chip
                            chipItem2.text = loyaltyMathRelationTextView.editText?.text.toString() + " "  + loyaltyValueInput.editText?.text.toString()
                            chipItem2.setOnCloseIconClickListener {
                                loyaltyChipGroup.removeView(chipItem)
                                loyaltyChipGroup.removeView(it)
                                if (loyaltyChipGroup.size == 0) loyaltyLayout.visibility = View.GONE
                                else if ((loyaltyChipGroup.get(0) as Chip).text == resources.getString(R.string.and) || (loyaltyChipGroup.get(0) as Chip).text == resources.getString(R.string.or)) loyaltyChipGroup.removeView(loyaltyChipGroup[0])
                            }
                            loyaltyChipGroup.addView(chipItem2)
                        }
                        else {
                            val inflater = LayoutInflater.from(this)
                            val chipItem = inflater.inflate(R.layout.card_parameter_chip_item,null,false) as Chip
                            chipItem.text = loyaltyMathRelationTextView.editText?.text.toString() + " " + loyaltyValueInput.editText?.text.toString()
                            chipItem.setOnCloseIconClickListener {
                                loyaltyChipGroup.removeView(it)
                                if (loyaltyChipGroup.size == 0) loyaltyLayout.visibility = View.GONE
                                else if ((loyaltyChipGroup.get(0) as Chip).text == resources.getString(R.string.and) || (loyaltyChipGroup.get(0) as Chip).text == resources.getString(R.string.or)) loyaltyChipGroup.removeView(loyaltyChipGroup[0])
                            }
                            loyaltyChipGroup.addView(chipItem)
                        }
                        (loyaltyValueInput.editText as? TextInputEditText)?.setText("")
                    }
                "Rarity" -> {
                        rarityLayout.visibility = View.VISIBLE
                        if (rarityChipGroup.size != 0) {
                            val inflater = LayoutInflater.from(this)
                            val chipItem = inflater.inflate(R.layout.card_parameter_and_or_chip_item, null, false) as Chip
                            chipItem.text = resources.getString(R.string.and)
                            chipItem.setOnClickListener { chipItem.text = if (chipItem.text == resources.getString(R.string.or)) resources.getString(R.string.and) else resources.getString(R.string.or) }
                            rarityChipGroup.addView(chipItem)
                            val inflater2 = LayoutInflater.from(this)
                            val chipItem2 = inflater2.inflate(R.layout.card_parameter_chip_item, null, false) as Chip
                            chipItem2.text = rarityValueInput.editText?.text.toString()
                            chipItem2.setOnCloseIconClickListener {
                                rarityChipGroup.removeView(chipItem)
                                rarityChipGroup.removeView(it)
                                if (rarityChipGroup.size == 0) rarityLayout.visibility = View.GONE
                                else if ((rarityChipGroup.get(0) as Chip).text == resources.getString(R.string.and) || (rarityChipGroup.get(0) as Chip).text == resources.getString(R.string.or)) rarityChipGroup.removeView(rarityChipGroup[0])
                            }
                            rarityChipGroup.addView(chipItem2)
                        }
                        else {
                            val inflater = LayoutInflater.from(this)
                            val chipItem = inflater.inflate(R.layout.card_parameter_chip_item,null,false) as Chip
                            chipItem.text = rarityValueInput.editText?.text.toString()
                            chipItem.setOnCloseIconClickListener {
                                rarityChipGroup.removeView(it)
                                if (rarityChipGroup.size == 0) rarityLayout.visibility = View.GONE
                                else if ((rarityChipGroup.get(0) as Chip).text == resources.getString(R.string.and) || (rarityChipGroup.get(0) as Chip).text == resources.getString(R.string.or)) rarityChipGroup.removeView(rarityChipGroup[0])
                            }
                            rarityChipGroup.addView(chipItem)
                        }
                    }
                "Legality" -> {
                        legalityLayout.visibility = View.VISIBLE
                        if (legalityChipGroup.size != 0) {
                            val inflater = LayoutInflater.from(this)
                            val chipItem = inflater.inflate(R.layout.card_parameter_and_or_chip_item, null, false) as Chip
                            chipItem.text = resources.getString(R.string.and)
                            chipItem.setOnClickListener { chipItem.text = if (chipItem.text == resources.getString(R.string.or)) resources.getString(R.string.and) else resources.getString(R.string.or) }
                            legalityChipGroup.addView(chipItem)
                            val inflater2 = LayoutInflater.from(this)
                            val chipItem2 = inflater2.inflate(R.layout.card_parameter_chip_item, null, false) as Chip
                            chipItem2.text = legalityTypeInput.editText?.text.toString() + " "  + legalityFormatInput.editText?.text.toString()
                            chipItem2.setOnCloseIconClickListener {
                                legalityChipGroup.removeView(chipItem)
                                legalityChipGroup.removeView(it)
                                if (legalityChipGroup.size == 0) legalityLayout.visibility = View.GONE
                                else if ((legalityChipGroup.get(0) as Chip).text == resources.getString(R.string.and) || (legalityChipGroup.get(0) as Chip).text == resources.getString(R.string.or)) legalityChipGroup.removeView(legalityChipGroup[0])
                            }
                            legalityChipGroup.addView(chipItem2)
                        }
                        else {
                            val inflater = LayoutInflater.from(this)
                            val chipItem = inflater.inflate(R.layout.card_parameter_chip_item,null,false) as Chip
                            chipItem.text = legalityTypeInput.editText?.text.toString() + " " + legalityFormatInput.editText?.text.toString()
                            chipItem.setOnCloseIconClickListener {
                                legalityChipGroup.removeView(it)
                                if (legalityChipGroup.size == 0) legalityLayout.visibility = View.GONE
                                else if ((legalityChipGroup.get(0) as Chip).text == resources.getString(R.string.and) || (legalityChipGroup.get(0) as Chip).text == resources.getString(R.string.or)) legalityChipGroup.removeView(legalityChipGroup[0])
                            }
                            legalityChipGroup.addView(chipItem)
                        }
                    }
                else -> {
                    layoutLayout.visibility = View.VISIBLE
                    if (layoutChipGroup.size != 0) {
                        val inflater = LayoutInflater.from(this)
                        val chipItem = inflater.inflate(R.layout.card_parameter_and_or_chip_item, null, false) as Chip
                        chipItem.text = resources.getString(R.string.and)
                        chipItem.setOnClickListener { chipItem.text = if (chipItem.text == resources.getString(R.string.or)) resources.getString(R.string.and) else resources.getString(R.string.or) }
                        layoutChipGroup.addView(chipItem)
                        val inflater2 = LayoutInflater.from(this)
                        val chipItem2 = inflater2.inflate(R.layout.card_parameter_chip_item, null, false) as Chip
                        chipItem2.text = layoutValueInput.editText?.text.toString()
                        chipItem2.setOnCloseIconClickListener {
                            layoutChipGroup.removeView(chipItem)
                            layoutChipGroup.removeView(it)
                            if (layoutChipGroup.size == 0) layoutLayout.visibility = View.GONE
                            else if ((layoutChipGroup.get(0) as Chip).text == resources.getString(R.string.and) || (layoutChipGroup.get(0) as Chip).text == resources.getString(R.string.or)) layoutChipGroup.removeView(layoutChipGroup[0])
                        }
                        layoutChipGroup.addView(chipItem2)
                    }
                    else {
                        val inflater = LayoutInflater.from(this)
                        val chipItem = inflater.inflate(R.layout.card_parameter_chip_item,null,false) as Chip
                        chipItem.text = layoutValueInput.editText?.text.toString()
                        chipItem.setOnCloseIconClickListener {
                            layoutChipGroup.removeView(it)
                            if (layoutChipGroup.size == 0) layoutLayout.visibility = View.GONE
                            else if ((layoutChipGroup.get(0) as Chip).text == resources.getString(R.string.and) || (layoutChipGroup.get(0) as Chip).text == resources.getString(R.string.or)) layoutChipGroup.removeView(layoutChipGroup[0])
                        }
                        layoutChipGroup.addView(chipItem)
                    }
                }
            }
        }

        val manaCostTextInput = findViewById<TextInputLayout>(R.id.mana_cost_text_input)

        val manaCostAddSymbolButton = findViewById<Button>(R.id.add_mana_symbol_button)
        manaCostAddSymbolButton.setOnClickListener {
            val manaCostSymbolDialog = LayoutInflater.from(this).inflate(R.layout.mana_cost_symbol_dialog, null, false)
            val dialog = MaterialAlertDialogBuilder(this)
                .setView(manaCostSymbolDialog)
                .setTitle(R.string.add_mana_symbol)
                .show()
            val costWLayout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_w_layout)
            costWLayout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{W}")
                dialog.dismiss()
            }
            val costULayout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_u_layout)
            costULayout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{U}")
                dialog.dismiss()
            }
            val costBLayout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_b_layout)
            costBLayout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{B}")
                dialog.dismiss()
            }
            val costRLayout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_r_layout)
            costRLayout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{R}")
                dialog.dismiss()
            }
            val costGLayout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_g_layout)
            costGLayout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{G}")
                dialog.dismiss()
            }
            val costWULayout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_wu_layout)
            costWULayout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{W/U}")
                dialog.dismiss()
            }
            val costUBLayout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_ub_layout)
            costUBLayout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{U/B}")
                dialog.dismiss()
            }
            val costBRLayout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_br_layout)
            costBRLayout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{B/R}")
                dialog.dismiss()
            }
            val costRGLayout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_rg_layout)
            costRGLayout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{R/G}")
                dialog.dismiss()
            }
            val costGWLayout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_gw_layout)
            costGWLayout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{G/W}")
                dialog.dismiss()
            }
            val costWBLayout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_wb_layout)
            costWBLayout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{W/B}")
                dialog.dismiss()
            }
            val costURLayout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_ur_layout)
            costURLayout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{U/R}")
                dialog.dismiss()
            }
            val costBGLayout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_bg_layout)
            costBGLayout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{B/G}")
                dialog.dismiss()
            }
            val costRWLayout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_rw_layout)
            costRWLayout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{R/W}")
                dialog.dismiss()
            }
            val costGULayout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_gu_layout)
            costGULayout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{G/U}")
                dialog.dismiss()
            }
            val costWPLayout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_wp_layout)
            costWPLayout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{W/P}")
                dialog.dismiss()
            }
            val costUPLayout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_up_layout)
            costUPLayout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{U/P}")
                dialog.dismiss()
            }
            val costBPLayout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_bp_layout)
            costBPLayout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{B/P}")
                dialog.dismiss()
            }
            val costRPLayout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_rp_layout)
            costRPLayout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{R/P}")
                dialog.dismiss()
            }
            val costGPLayout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_gp_layout)
            costGPLayout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{G/P}")
                dialog.dismiss()
            }
            val cost2WLayout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_2w_layout)
            cost2WLayout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{2/W}")
                dialog.dismiss()
            }
            val cost2ULayout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_2u_layout)
            cost2ULayout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{2/U}")
                dialog.dismiss()
            }
            val cost2BLayout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_2b_layout)
            cost2BLayout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{2/B}")
                dialog.dismiss()
            }
            val cost2RLayout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_2r_layout)
            cost2RLayout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{2/R}")
                dialog.dismiss()
            }
            val cost2GLayout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_2g_layout)
            cost2GLayout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{2/G}")
                dialog.dismiss()
            }
            val costCLayout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_c_layout)
            costCLayout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{C}")
                dialog.dismiss()
            }
            val costSLayout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_s_layout)
            costSLayout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{S}")
                dialog.dismiss()
            }
            val cost0Layout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_0_layout)
            cost0Layout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{0}")
                dialog.dismiss()
            }
            val cost1Layout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_1_layout)
            cost1Layout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{1}")
                dialog.dismiss()
            }
            val cost2Layout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_2_layout)
            cost2Layout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{2}")
                dialog.dismiss()
            }
            val cost3Layout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_3_layout)
            cost3Layout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{3}")
                dialog.dismiss()
            }
            val cost4Layout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_4_layout)
            cost4Layout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{4}")
                dialog.dismiss()
            }
            val cost5Layout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_5_layout)
            cost5Layout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{5}")
                dialog.dismiss()
            }
            val cost6Layout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_6_layout)
            cost6Layout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{6}")
                dialog.dismiss()
            }
            val cost7Layout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_7_layout)
            cost7Layout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{7}")
                dialog.dismiss()
            }
            val cost8Layout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_8_layout)
            cost8Layout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{8}")
                dialog.dismiss()
            }
            val cost9Layout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_9_layout)
            cost9Layout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{9}")
                dialog.dismiss()
            }
            val cost10Layout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_10_layout)
            cost10Layout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{10}")
                dialog.dismiss()
            }
            val cost11Layout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_11_layout)
            cost11Layout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{11}")
                dialog.dismiss()
            }
            val cost12Layout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_12_layout)
            cost12Layout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{12}")
                dialog.dismiss()
            }
            val cost13Layout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_13_layout)
            cost13Layout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{13}")
                dialog.dismiss()
            }
            val cost15Layout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_15_layout)
            cost15Layout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{15}")
                dialog.dismiss()
            }
            val cost16Layout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_16_layout)
            cost16Layout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{16}")
                dialog.dismiss()
            }
            val costXLayout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_x_layout)
            costXLayout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{X}")
                dialog.dismiss()
            }
            val costGUPLayout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_gup_layout)
            costGUPLayout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{G/U/P}")
                dialog.dismiss()
            }
            val costYLayout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_y_layout)
            costYLayout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{Y}")
                dialog.dismiss()
            }
            val costZLayout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_z_layout)
            costZLayout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{Z}")
                dialog.dismiss()
            }
            val cost1000000Layout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_1000000_layout)
            cost1000000Layout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{1000000}")
                dialog.dismiss()
            }
            val costHWLayout = manaCostSymbolDialog.findViewById<LinearLayout>(R.id.cost_hw_layout)
            costHWLayout.setOnClickListener {
                manaCostTextInput.editText?.setText(manaCostTextInput.editText?.text.toString() + "{HW}")
                dialog.dismiss()
            }
        }

        val colorOperatorToggle = findViewById<MaterialButtonToggleGroup>(R.id.color_operator_toggle_group)
        colorOperatorToggle.check(R.id.color_exactly_button)

        val colorToggleGroup = findViewById<MaterialButtonToggleGroup>(R.id.color_toggle_group)
        colorToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                if (checkedId == R.id.color_colorless_button) {
                    group.clearChecked()
                    group.check(checkedId)
                } else {
                    group.uncheck(R.id.color_colorless_button)
                }
            }
        }

        val colorIdentityToggleGroup = findViewById<MaterialButtonToggleGroup>(R.id.color_identity_toggle_group)
        colorIdentityToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                if (checkedId == R.id.color_identity_colorless_button) {
                    group.clearChecked()
                    group.check(checkedId)
                } else {
                    group.uncheck(R.id.color_identity_colorless_button)
                }
            }
        }

        val producedManaToggleGroup = findViewById<MaterialButtonToggleGroup>(R.id.produced_mana_toggle_group)

        val cardFlavorTextTextView = findViewById<TextInputLayout>(R.id.card_flavor_text_search)

        val priceCoinToggle = findViewById<MaterialButtonToggleGroup>(R.id.price_coin_toggle_group)
        priceCoinToggle.check(R.id.price_usd_button)

        val priceMathRelationTextView = findViewById<TextInputLayout>(R.id.price_math_relation_selector)
        val priceMathAdapter = ArrayAdapter(applicationContext, R.layout.list_item, parametersMathValues)
        (priceMathRelationTextView.editText as? AutoCompleteTextView)?.setAdapter(priceMathAdapter)
        (priceMathRelationTextView.editText as? AutoCompleteTextView)?.setText(parametersMathValues[0], false)

        val priceValueInput = findViewById<TextInputLayout>(R.id.price_value_input)

        val setTextView = findViewById<TextInputLayout>(R.id.set_text_input)
        var setCatalog: MutableList<String> = dbHelper.getSetCatalog()
        var setAdapter = ArrayAdapter(applicationContext, R.layout.list_item, setCatalog)
        (setTextView.editText as? AutoCompleteTextView)?.setAdapter(setAdapter)

        val artistTextView = findViewById<TextInputLayout>(R.id.artist_text_input)
        var artistCatalog: MutableList<String> = dbHelper.getArtistCatalog()
        var artistAdapter = ArrayAdapter(applicationContext, R.layout.list_item, artistCatalog)
        (artistTextView.editText as? AutoCompleteTextView)?.setAdapter(artistAdapter)

        val searchButton = findViewById<FloatingActionButton>(R.id.search_fab)
        searchButton.setOnClickListener {
            var cardTypesList = mutableListOf<String>()
            var isCardTypesList = mutableListOf<Boolean>()
            var k = 0
            while (k < cardTypesChipGroup.size) {
                val chip = cardTypesChipGroup.get(k) as Chip
                cardTypesList.add(chip.text.toString())
                isCardTypesList.add(chip.isChecked)
                ++k
            }
            var manaValueParameters = mutableListOf<String>()
            k = 0
            while (k < manaValueChipGroup.size) {
                val chip = manaValueChipGroup.get(k) as Chip
                manaValueParameters.add(chip.text.toString())
                ++k
            }
            var powerParameters = mutableListOf<String>()
            k = 0
            while (k < powerChipGroup.size) {
                val chip = powerChipGroup.get(k) as Chip
                powerParameters.add(chip.text.toString())
                ++k
            }
            var toughnessParameters = mutableListOf<String>()
            k = 0
            while (k < toughnessChipGroup.size) {
                val chip = toughnessChipGroup.get(k) as Chip
                toughnessParameters.add(chip.text.toString())
                ++k
            }
            var loyaltyParameters = mutableListOf<String>()
            k = 0
            while (k < loyaltyChipGroup.size) {
                val chip = loyaltyChipGroup.get(k) as Chip
                loyaltyParameters.add(chip.text.toString())
                ++k
            }
            var rarityParameters = mutableListOf<String>()
            k = 0
            while (k < rarityChipGroup.size) {
                val chip = rarityChipGroup.get(k) as Chip
                rarityParameters.add(chip.text.toString())
                ++k
            }
            var legalityParameters = mutableListOf<String>()
            k = 0
            while (k < legalityChipGroup.size) {
                val chip = legalityChipGroup.get(k) as Chip
                legalityParameters.add(chip.text.toString())
                ++k
            }
            var layoutParameters = mutableListOf<String>()
            k = 0
            while (k < layoutChipGroup.size) {
                val chip = layoutChipGroup.get(k) as Chip
                layoutParameters.add(chip.text.toString())
                ++k
            }
            val colorOperator = if (colorOperatorToggle.checkedButtonId == R.id.color_exactly_button) "exactly" else if (colorOperatorToggle.checkedButtonId == R.id.color_including_button) "including" else "at_most"

            val intent = Intent (this, CardListActivity::class.java)
            intent.putExtra("card_name", nameTextView.editText?.text.toString())
            intent.putExtra("card_types", cardTypesList.toTypedArray())
            intent.putExtra("is_card_types", isCardTypesList.toBooleanArray())
            intent.putExtra("card_type_and", (cardTypesAndOrChipGroup.get(0) as Chip).isChecked)
            intent.putExtra("card_text", cardTextTextView.editText?.text.toString())
            intent.putExtra("mana_value_params", manaValueParameters.toTypedArray())
            intent.putExtra("power_params", powerParameters.toTypedArray())
            intent.putExtra("toughness_params", toughnessParameters.toTypedArray())
            intent.putExtra("loyalty_params", loyaltyParameters.toTypedArray())
            intent.putExtra("rarity_params", rarityParameters.toTypedArray())
            intent.putExtra("legality_params", legalityParameters.toTypedArray())
            intent.putExtra("layout_params", layoutParameters.toTypedArray())
            intent.putExtra("mana_cost", manaCostTextInput.editText?.text.toString())
            intent.putExtra("color", getColorsSelectedArray(colorToggleGroup, "color"))
            intent.putExtra("color_operator", colorOperator)
            intent.putExtra("color_identity", getColorsSelectedArray(colorIdentityToggleGroup, "identity"))
            intent.putExtra("produced_mana", getColorsSelectedArray(producedManaToggleGroup, "produced"))
            intent.putExtra("card_flavor_text", cardFlavorTextTextView.editText?.text.toString())
            intent.putExtra("price_coin", if (priceCoinToggle.checkedButtonId == R.id.price_usd_button) "usd" else if (priceCoinToggle.checkedButtonId == R.id.price_eur_button) "eur" else "tix")
            intent.putExtra("price_operator", priceMathRelationTextView.editText?.text.toString())
            intent.putExtra("price_value", priceValueInput.editText?.text.toString())
            intent.putExtra("card_set", setTextView.editText?.text.toString())
            intent.putExtra("card_artist", artistTextView.editText?.text.toString())
            startActivity(intent)
        }
    }

    fun getColorsSelectedArray(colorToggleGroup: MaterialButtonToggleGroup, type: String): String {
        var result = ""
        val checkedIds = colorToggleGroup.checkedButtonIds
        if (checkedIds.size < 1) return result
        else {
            if (type == "color") {
                checkedIds.forEach {
                    when (it) {
                        R.id.color_white_button -> result += if (result.length < 1) "W" else ",W"
                        R.id.color_blue_button -> result += if (result.length < 1) "U" else ",U"
                        R.id.color_black_button -> result += if (result.length < 1) "B" else ",B"
                        R.id.color_red_button -> result += if (result.length < 1) "R" else ",R"
                        R.id.color_green_button -> result += if (result.length < 1) "G" else ",G"
                        else -> result = "C"
                    }
                }
            }
            else if (type == "identity") {
                checkedIds.forEach {
                    when (it) {
                        R.id.color_identity_white_button -> result += if (result.length < 1) "W" else ",W"
                        R.id.color_identity_blue_button -> result += if (result.length < 1) "U" else ",U"
                        R.id.color_identity_black_button -> result += if (result.length < 1) "B" else ",B"
                        R.id.color_identity_red_button -> result += if (result.length < 1) "R" else ",R"
                        R.id.color_identity_green_button -> result += if (result.length < 1) "G" else ",G"
                        else -> result = "C"
                    }
                }
            }
            else {
                checkedIds.forEach {
                    when (it) {
                        R.id.produced_mana_white_button -> result += if (result.length < 1) "W" else ",W"
                        R.id.produced_mana_blue_button -> result += if (result.length < 1) "U" else ",U"
                        R.id.produced_mana_black_button -> result += if (result.length < 1) "B" else ",B"
                        R.id.produced_mana_red_button -> result += if (result.length < 1) "R" else ",R"
                        R.id.produced_mana_green_button -> result += if (result.length < 1) "G" else ",G"
                        else -> result += if (result.length < 1) "C" else ",C"
                    }
                }
            }
        }
        return result
    }

    fun getSymbolDrawable (str: String): Drawable {
        when (str) {
            "{T}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_t_cost)!!
            "{Q}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_q_cost)!!
            "{E}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_e_cost)!!
            "{PW}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_pw_cost)!!
            "{CHAOS}" -> return AppCompatResources.getDrawable(
                this,
                R.drawable.ic_chaos_cost
            )!!
            "{A}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_a_cost)!!
            "{X}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_x_cost)!!
            "{Y}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_y_cost)!!
            "{Z}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_z_cost)!!
            "{0}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_0_cost)!!
            "{1}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_1_cost)!!
            "{2}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_2_cost)!!
            "{3}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_3_cost)!!
            "{4}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_4_cost)!!
            "{5}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_5_cost)!!
            "{6}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_6_cost)!!
            "{7}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_7_cost)!!
            "{8}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_8_cost)!!
            "{9}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_9_cost)!!
            "{10}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_10_cost)!!
            "{11}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_11_cost)!!
            "{12}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_12_cost)!!
            "{13}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_13_cost)!!
            "{14}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_14_cost)!!
            "{15}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_15_cost)!!
            "{16}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_16_cost)!!
            "{17}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_17_cost)!!
            "{18}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_18_cost)!!
            "{19}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_19_cost)!!
            "{20}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_20_cost)!!
            "{100}" -> return AppCompatResources.getDrawable(
                this,
                R.drawable.ic_100_cost
            )!!
            "{1000000}" -> return AppCompatResources.getDrawable(
                this,
                R.drawable.ic_1000000_cost
            )!!
            "{∞}" -> return AppCompatResources.getDrawable(
                this,
                R.drawable.ic_infinity_cost
            )!!
            "{W/U}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_wu_cost)!!
            "{W/B}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_wb_cost)!!
            "{B/R}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_br_cost)!!
            "{B/G}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_bg_cost)!!
            "{U/B}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_ub_cost)!!
            "{U/R}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_ur_cost)!!
            "{R/G}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_rg_cost)!!
            "{R/W}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_rw_cost)!!
            "{G/W}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_gw_cost)!!
            "{G/U}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_gu_cost)!!
            "{W/U/P}" -> return AppCompatResources.getDrawable(
                this,
                R.drawable.ic_wup_cost
            )!!
            "{W/B/P}" -> return AppCompatResources.getDrawable(
                this,
                R.drawable.ic_wbp_cost
            )!!
            "{B/R/P}" -> return AppCompatResources.getDrawable(
                this,
                R.drawable.ic_brp_cost
            )!!
            "{B/G/P}" -> return AppCompatResources.getDrawable(
                this,
                R.drawable.ic_bgp_cost
            )!!
            "{U/B/P}" -> return AppCompatResources.getDrawable(
                this,
                R.drawable.ic_ubp_cost
            )!!
            "{U/R/P}" -> return AppCompatResources.getDrawable(
                this,
                R.drawable.ic_urp_cost
            )!!
            "{R/G/P}" -> return AppCompatResources.getDrawable(
                this,
                R.drawable.ic_rgp_cost
            )!!
            "{R/W/P}" -> return AppCompatResources.getDrawable(
                this,
                R.drawable.ic_rwp_cost
            )!!
            "{G/W/P}" -> return AppCompatResources.getDrawable(
                this,
                R.drawable.ic_gwp_cost
            )!!
            "{G/U/P}" -> return AppCompatResources.getDrawable(
                this,
                R.drawable.ic_gup_cost
            )!!
            "{2W}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_2w_cost)!!
            "{2U}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_2u_cost)!!
            "{2B}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_2b_cost)!!
            "{2R}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_2r_cost)!!
            "{2G}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_2g_cost)!!
            "{W}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_w_cost)!!
            "{U}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_u_cost)!!
            "{B}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_b_cost)!!
            "{R}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_r_cost)!!
            "{G}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_g_cost)!!
            "{P}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_p_cost)!!
            "{W/P}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_wp_cost)!!
            "{U/P}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_up_cost)!!
            "{B/P}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_bp_cost)!!
            "{R/P}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_rp_cost)!!
            "{G/P}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_gp_cost)!!
            "{C}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_c_cost)!!
            "{S}" -> return AppCompatResources.getDrawable(this, R.drawable.ic_s_cost)!!
            else -> return AppCompatResources.getDrawable(this, R.drawable.ic_chaos_cost)!!
        }
    }
}