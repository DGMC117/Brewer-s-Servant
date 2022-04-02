package magicchief.main.brewersservant

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.view.get
import androidx.core.view.size
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

        val nameTextView = findViewById<TextInputLayout>(R.id.card_name)

        val cardTypesDivider = findViewById<MaterialDivider>(R.id.card_type_divider)
        val cardTypesChipGroup = findViewById<ChipGroup>(R.id.card_type_chip_group)

        val typeLineTextView = findViewById<TextInputLayout>(R.id.card_type_text)
        val cardTypes = listOf("Advisor", "Aetherborn", "Ally", "Angel", "Antelope", "Ape", "Archer", "Archon", "Army", "Artificer", "Assassin", "Assembly-Worker", "Atog", "Aurochs", "Avatar", "Azra", "Badger", "Barbarian", "Bard", "Basilisk", "Bat", "Bear", "Beast", "Beeble", "Beholder", "Berserker", "Bird", "Blinkmoth", "Boar", "Bringer", "Brushwagg", "Camarid", "Camel", "Caribou", "Carrier", "Cat", "Centaur", "Cephalid", "Chicken", "Chimera", "Citizen", "Cleric", "Cockatrice", "Construct", "Coward", "Crab", "Crocodile", "Cyclops", "Dauthi", "Demigod", "Demon", "Deserter", "Devil", "Dinosaur", "Djinn", "Dog", "Dragon", "Drake", "Dreadnought", "Drone", "Druid", "Dryad", "Dwarf", "Efreet", "Egg", "Elder", "Eldrazi", "Elemental", "Elephant", "Elf", "Elk", "Eye", "Faerie", "Ferret", "Fish", "Flagbearer", "Fox", "Fractal", "Frog", "Fungus", "Gargoyle", "Germ", "Giant", "Gnoll", "Gnome", "Goat", "Goblin", "God", "Golem", "Gorgon", "Graveborn", "Gremlin", "Griffin", "Guest", "Hag", "Halfling", "Hamster", "Harpy", "Head", "Hellion", "Hippo", "Hippogriff", "Homarid", "Homunculus", "Hornet", "Horror", "Horse", "Human", "Hydra", "Hyena", "Illusion", "Imp", "Incarnation", "Inkling", "Insect", "Jackal", "Jellyfish", "Juggernaut", "Kavu", "Kirin", "Kithkin", "Knight", "Kobold", "Kor", "Kraken", "Lamia", "Lammasu", "Leech", "Leviathan", "Lhurgoyf", "Licid", "Lizard", "Manticore", "Masticore", "Mercenary", "Merfolk", "Metathran", "Minion", "Minotaur", "Mole", "Monger", "Mongoose", "Monk", "Monkey", "Moonfolk", "Mouse", "Mutant", "Myr", "Mystic", "Naga", "Nautilus", "Nephilim", "Nightmare", "Nightstalker", "Ninja", "Noble", "Noggle", "Nomad", "Nymph", "Octopus", "Ogre", "Ooze", "Orb", "Orc", "Orgg", "Otter", "Ouphe", "Ox", "Oyster", "Pangolin", "Peasant", "Pegasus", "Pentavite", "Pest", "Phelddagrif", "Phoenix", "Phyrexian", "Pilot", "Pincher", "Pirate", "Plant", "Praetor", "Prism", "Processor", "Rabbit", "Ranger", "Rat", "Rebel", "Reflection", "Reveler", "Rhino", "Rigger", "Rogue", "Sable", "Salamander", "Samurai", "Sand", "Saproling", "Satyr", "Scarecrow", "Scion", "Scorpion", "Scout", "Sculpture", "Serf", "Serpent", "Servo", "Shade", "Shaman", "Shapeshifter", "Shark", "Sheep", "Siren", "Skeleton", "Slith", "Sliver", "Slug", "Snake", "Soldier", "Soltari", "Spawn", "Specter", "Spellshaper", "Sphinx", "Spider", "Spike", "Spirit", "Splinter", "Sponge", "Squid", "Squirrel", "Starfish", "Surrakar", "Survivor", "Tentacle", "Tetravite", "Thalakos", "Thopter", "Thrull", "Tiefling", "Treefolk", "Trilobite", "Triskelavite", "Troll", "Turtle", "Unicorn", "Vampire", "Vedalken", "Viashino", "Volver", "Wall", "Warlock", "Warrior", "Wasp", "Weird", "Werewolf", "Whale", "Wizard", "Wolf", "Wolverine", "Wombat", "Worm", "Wraith", "Wurm", "Yeti", "Zombie", "Zubera", "Abian", "Ajani", "Aminatou", "Angrath", "Arlinn", "Ashiok", "B.O.B.", "Bahamut", "Basri", "Bolas", "Calix", "Chandra", "Dack", "Dakkon", "Daretti", "Davriel", "Dihada", "Domri", "Dovin", "Duck", "Dungeon", "Ellywick", "Elspeth", "Estrid", "Freyalise", "Garruk", "Gideon", "Grist", "Huatli", "Inzerva", "Jace", "Jaya", "Jeska", "Kaito", "Karn", "Kasmina", "Kaya", "Kiora", "Koth", "Liliana", "Lolth", "Lukka", "Master", "Mordenkainen", "Nahiri", "Narset", "Niko", "Nissa", "Nixilis", "Oko", "Ral", "Rowan", "Saheeli", "Samut", "Sarkhan", "Serra", "Sorin", "Szat", "Tamiyo", "Teferi", "Teyo", "Tezzeret", "Tibalt", "Tyvar", "Ugin", "Urza", "Venser", "Vivien", "Vraska", "Will", "Windgrace", "Wrenn", "Xenagos", "Yanggu", "Yanling", "Zariel", "Desert", "Forest", "Gate", "Island", "Lair", "Locus", "Mine", "Mountain", "Plains", "Power-Plant", "Swamp", "Tower", "Urza’s", "Blood", "Clue", "Contraption", "Equipment", "Food", "Fortification", "Gold", "Treasure", "Vehicle", "Aura", "Cartouche", "Class", "Curse", "Rune", "Saga", "Shard", "Shrine", "Adventure", "Arcane", "Lesson", "Trap")
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

        val manaCostSymbols = arrayOf("\uE600")
        val manaCostAddSymbolButton = findViewById<Button>(R.id.add_mana_symbol_button)
        manaCostAddSymbolButton.setOnClickListener {
            MaterialAlertDialogBuilder(this, R.style.SymbolChoiceDialog).setTitle(R.string.add_mana_symbol)
                .setItems(manaCostSymbols){ dialog, which ->
                    Toast.makeText(applicationContext, "w", Toast.LENGTH_SHORT).show()
                }.show()
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
            intent.putExtra("color", getColorsSelectedArray(colorToggleGroup, "color"))
            intent.putExtra("color_operator", colorOperator)
            intent.putExtra("color_identity", getColorsSelectedArray(colorIdentityToggleGroup, "identity"))
            intent.putExtra("produced_mana", getColorsSelectedArray(producedManaToggleGroup, "produced"))
            intent.putExtra("card_flavor_text", cardFlavorTextTextView.editText?.text.toString())
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
}