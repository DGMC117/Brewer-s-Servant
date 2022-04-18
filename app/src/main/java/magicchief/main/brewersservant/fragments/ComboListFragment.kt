package magicchief.main.brewersservant.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import magicchief.main.brewersservant.CardListAdapter
import magicchief.main.brewersservant.ComboListAdapter
import magicchief.main.brewersservant.DBHelper
import magicchief.main.brewersservant.R
class ComboListFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<ComboListAdapter.ViewHolder>? = null

    var cardNamesArray: Array<String>? = null
    var cardNamesAnd: Boolean = true
    var comboColorOperator: String? = null
    var comboColor: String? = null
    var comboResult: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cardNamesArray = it.getStringArray("cardNamesArray")
            cardNamesAnd = it.getBoolean("cardNamesAnd")
            comboColorOperator = it.getString("comboColorOperator").toString()
            comboColor = it.getString("comboColor").toString()
            comboResult = it.getString("comboResult").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_combo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val noResultstext = requireView().findViewById<TextView>(R.id.combo_list_no_results)

        val db = DBHelper(requireContext())
        val combos = db.getCombos(cardNamesArray, cardNamesAnd, comboColorOperator, comboColor, comboResult)

        val comboList = requireView().findViewById<RecyclerView>(R.id.combo_list)
        val divider = DividerItemDecoration (requireContext(), DividerItemDecoration.VERTICAL)
        divider.setDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.divider)!!)
        comboList.addItemDecoration(divider)
        layoutManager = LinearLayoutManager (requireContext())
        comboList.layoutManager = layoutManager
        adapter = ComboListAdapter(combos, requireContext())
        comboList.adapter = adapter
        if (combos.isEmpty()) noResultstext.visibility = View.VISIBLE
    }
}