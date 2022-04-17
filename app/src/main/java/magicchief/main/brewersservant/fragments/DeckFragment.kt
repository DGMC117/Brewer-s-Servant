package magicchief.main.brewersservant.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import magicchief.main.brewersservant.R

class DeckFragment : Fragment() {

    var deckId = 1

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
        return inflater.inflate(R.layout.fragment_deck, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayout = requireView().findViewById<TabLayout>(R.id.deck_tab_layout)
        val viewPager2 = requireView().findViewById<ViewPager2>(R.id.deck_view_pager)

        val fragmentManager = childFragmentManager
        val pagerAdapter = ViewPagerAdapter(fragmentManager, lifecycle)
        pagerAdapter.addFragment(DeckCardsFragment(), deckId)
        pagerAdapter.addFragment(DeckEditFragment(), deckId)
        pagerAdapter.addFragment(DeckStatsFragment(), deckId)
        viewPager2.adapter = pagerAdapter

        tabLayout.addTab(tabLayout.newTab().setText("Cards").setIcon(R.drawable.ic_card))
        tabLayout.addTab(tabLayout.newTab().setText("Edit").setIcon(R.drawable.ic_baseline_edit_24))
        tabLayout.addTab(tabLayout.newTab().setText("Stats").setIcon(R.drawable.ic_baseline_bar_chart_24))

        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager2.setCurrentItem(tab.position)
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

    }

    private class ViewPagerAdapter : FragmentStateAdapter {

        var childFragments: MutableList<Fragment> = ArrayList()

        constructor(@NonNull fragmentManager: FragmentManager, @NonNull lifecycle: Lifecycle) : super(fragmentManager, lifecycle)

        @NonNull override fun createFragment(position: Int): Fragment {
            return childFragments[position]
        }

        override fun getItemCount(): Int {
            return childFragments.size
        }

        fun addFragment(frag: Fragment, deckId: Int) {
            val args = Bundle()
            args.putInt("deckId", deckId)
            frag.arguments = args
            childFragments.add(frag)
        }
    }
}