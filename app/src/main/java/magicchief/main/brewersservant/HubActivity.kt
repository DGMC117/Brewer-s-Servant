package magicchief.main.brewersservant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import magicchief.main.brewersservant.fragments.*

class HubActivity : AppCompatActivity() {

    private val cardSearchFragment = CardSearchFragment()
    private val comboSearchFragment = ComboSearchFragment()
    private val decksListFragment = DecksListFragment()
    private val settingsFragment = SettingsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hub)
        replaceFragment(cardSearchFragment)

        val hubBottomNavigation = findViewById<BottomNavigationView>(R.id.hub_bottom_navigation_bar)
        hubBottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.card_search_menu_item -> replaceFragment(cardSearchFragment)
                R.id.combo_search_menu_item -> replaceFragment(comboSearchFragment)
                R.id.decks_menu_item -> replaceFragment(decksListFragment)
                R.id.settings_menu_item -> replaceFragment(settingsFragment)
            }
            true
        }
    }

    private fun replaceFragment (fragment: Fragment) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.hub_fragment_container, fragment)
            transaction.commit()
        }
    }
}