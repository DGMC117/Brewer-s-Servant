package magicchief.main.brewersservant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.view.get
import androidx.core.view.size
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout

class CardSearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_search)

        val nameTextView = findViewById<TextInputLayout>(R.id.card_name)

        val cardTypesChipGroup = findViewById<ChipGroup>(R.id.card_type_chip_group)

        val typeLineTextView = findViewById<TextInputLayout>(R.id.card_type_text)
        val cardTypes = listOf("Advisor", "Aetherborn", "Ally", "Angel", "Antelope", "Ape", "Archer", "Archon", "Army", "Artificer", "Assassin", "Assembly-Worker", "Atog", "Aurochs", "Avatar", "Azra", "Badger", "Barbarian", "Bard", "Basilisk", "Bat", "Bear", "Beast", "Beeble", "Beholder", "Berserker", "Bird", "Blinkmoth", "Boar", "Bringer", "Brushwagg", "Camarid", "Camel", "Caribou", "Carrier", "Cat", "Centaur", "Cephalid", "Chicken", "Chimera", "Citizen", "Cleric", "Cockatrice", "Construct", "Coward", "Crab", "Crocodile", "Cyclops", "Dauthi", "Demigod", "Demon", "Deserter", "Devil", "Dinosaur", "Djinn", "Dog", "Dragon", "Drake", "Dreadnought", "Drone", "Druid", "Dryad", "Dwarf", "Efreet", "Egg", "Elder", "Eldrazi", "Elemental", "Elephant", "Elf", "Elk", "Eye", "Faerie", "Ferret", "Fish", "Flagbearer", "Fox", "Fractal", "Frog", "Fungus", "Gargoyle", "Germ", "Giant", "Gnoll", "Gnome", "Goat", "Goblin", "God", "Golem", "Gorgon", "Graveborn", "Gremlin", "Griffin", "Guest", "Hag", "Halfling", "Hamster", "Harpy", "Head", "Hellion", "Hippo", "Hippogriff", "Homarid", "Homunculus", "Hornet", "Horror", "Horse", "Human", "Hydra", "Hyena", "Illusion", "Imp", "Incarnation", "Inkling", "Insect", "Jackal", "Jellyfish", "Juggernaut", "Kavu", "Kirin", "Kithkin", "Knight", "Kobold", "Kor", "Kraken", "Lamia", "Lammasu", "Leech", "Leviathan", "Lhurgoyf", "Licid", "Lizard", "Manticore", "Masticore", "Mercenary", "Merfolk", "Metathran", "Minion", "Minotaur", "Mole", "Monger", "Mongoose", "Monk", "Monkey", "Moonfolk", "Mouse", "Mutant", "Myr", "Mystic", "Naga", "Nautilus", "Nephilim", "Nightmare", "Nightstalker", "Ninja", "Noble", "Noggle", "Nomad", "Nymph", "Octopus", "Ogre", "Ooze", "Orb", "Orc", "Orgg", "Otter", "Ouphe", "Ox", "Oyster", "Pangolin", "Peasant", "Pegasus", "Pentavite", "Pest", "Phelddagrif", "Phoenix", "Phyrexian", "Pilot", "Pincher", "Pirate", "Plant", "Praetor", "Prism", "Processor", "Rabbit", "Ranger", "Rat", "Rebel", "Reflection", "Reveler", "Rhino", "Rigger", "Rogue", "Sable", "Salamander", "Samurai", "Sand", "Saproling", "Satyr", "Scarecrow", "Scion", "Scorpion", "Scout", "Sculpture", "Serf", "Serpent", "Servo", "Shade", "Shaman", "Shapeshifter", "Shark", "Sheep", "Siren", "Skeleton", "Slith", "Sliver", "Slug", "Snake", "Soldier", "Soltari", "Spawn", "Specter", "Spellshaper", "Sphinx", "Spider", "Spike", "Spirit", "Splinter", "Sponge", "Squid", "Squirrel", "Starfish", "Surrakar", "Survivor", "Tentacle", "Tetravite", "Thalakos", "Thopter", "Thrull", "Tiefling", "Treefolk", "Trilobite", "Triskelavite", "Troll", "Turtle", "Unicorn", "Vampire", "Vedalken", "Viashino", "Volver", "Wall", "Warlock", "Warrior", "Wasp", "Weird", "Werewolf", "Whale", "Wizard", "Wolf", "Wolverine", "Wombat", "Worm", "Wraith", "Wurm", "Yeti", "Zombie", "Zubera", "Abian", "Ajani", "Aminatou", "Angrath", "Arlinn", "Ashiok", "B.O.B.", "Bahamut", "Basri", "Bolas", "Calix", "Chandra", "Dack", "Dakkon", "Daretti", "Davriel", "Dihada", "Domri", "Dovin", "Duck", "Dungeon", "Ellywick", "Elspeth", "Estrid", "Freyalise", "Garruk", "Gideon", "Grist", "Huatli", "Inzerva", "Jace", "Jaya", "Jeska", "Kaito", "Karn", "Kasmina", "Kaya", "Kiora", "Koth", "Liliana", "Lolth", "Lukka", "Master", "Mordenkainen", "Nahiri", "Narset", "Niko", "Nissa", "Nixilis", "Oko", "Ral", "Rowan", "Saheeli", "Samut", "Sarkhan", "Serra", "Sorin", "Szat", "Tamiyo", "Teferi", "Teyo", "Tezzeret", "Tibalt", "Tyvar", "Ugin", "Urza", "Venser", "Vivien", "Vraska", "Will", "Windgrace", "Wrenn", "Xenagos", "Yanggu", "Yanling", "Zariel", "Desert", "Forest", "Gate", "Island", "Lair", "Locus", "Mine", "Mountain", "Plains", "Power-Plant", "Swamp", "Tower", "Urza’s", "Blood", "Clue", "Contraption", "Equipment", "Food", "Fortification", "Gold", "Treasure", "Vehicle", "Aura", "Cartouche", "Class", "Curse", "Rune", "Saga", "Shard", "Shrine", "Adventure", "Arcane", "Lesson", "Trap")
        val adapter = ArrayAdapter(applicationContext, R.layout.list_item, cardTypes)
        (typeLineTextView.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        (typeLineTextView.editText as? AutoCompleteTextView)?.setOnItemClickListener { adapterView, view, i, l ->
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
                }
                cardTypesChipGroup.addView(chipItem)
                chipItem.isChecked = true
            }
            else Toast.makeText(applicationContext, R.string.card_type_repeated, Toast.LENGTH_SHORT).show()
            (typeLineTextView.editText as? AutoCompleteTextView)?.setText("")
        }

        val cardTypesAndOrChipGroup = findViewById<ChipGroup>(R.id.type_line_and_or_chip_group)

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
            val intent = Intent (this, CardListActivity::class.java)
            intent.putExtra("card_name", nameTextView.editText?.text.toString())
            intent.putExtra("card_types", cardTypesList.toTypedArray())
            intent.putExtra("is_card_types", isCardTypesList.toBooleanArray())
            intent.putExtra("card_type_and", (cardTypesAndOrChipGroup.get(0) as Chip).isChecked)
            startActivity(intent)
        }
    }
}