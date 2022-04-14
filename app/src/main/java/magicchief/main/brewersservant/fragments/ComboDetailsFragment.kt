package magicchief.main.brewersservant.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.*
import magicchief.main.brewersservant.*

class ComboDetailsFragment : Fragment() {

    var comboId = 1

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<ComboDetailsNamesAdapter.ViewHolder>? = null

    private var layoutManagerImages: RecyclerView.LayoutManager? = null
    private var adapterImages: RecyclerView.Adapter<ComboDetailsImagesAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            comboId = it.getInt("comboId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_combo_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = DBHelper (requireContext())
        val combo = db.getCombo (comboId)
        val cards = db.getComboCards (comboId)
        val cardImages: MutableList<String> = ArrayList()
        cards.forEach {
            if (it.image_uris != null && it.image_uris.border_crop != null && it.image_uris.border_crop?.toString() != "null") cardImages.add (it.image_uris.border_crop.toString())
            else cardImages.add (it.card_faces?.get(0)?.image_uris?.border_crop.toString())
        }

        val cardNamesRecycler = requireView().findViewById<RecyclerView>(R.id.combo_details_card_names_recycler)
        val cardImagesRecycler = requireView().findViewById<RecyclerView>(R.id.combo_details_card_images_recycler)
        val colorIdentity = requireView().findViewById<TextView>(R.id.combo_details_color_identity)
        val prerequisitesTextView = requireView().findViewById<TextView>(R.id.combo_details_prerequisites)
        val stepsTextView = requireView().findViewById<TextView>(R.id.combo_details_steps)
        val resultsTextView = requireView().findViewById<TextView>(R.id.combo_details_results)

        layoutManager = LinearLayoutManager (requireContext())
        cardNamesRecycler.layoutManager = layoutManager
        adapter = ComboDetailsNamesAdapter(combo.cards!!, requireContext())
        cardNamesRecycler.adapter = adapter

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(cardImagesRecycler)
        layoutManagerImages = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        cardImagesRecycler.layoutManager = layoutManagerImages
        adapterImages = ComboDetailsImagesAdapter(cardImages, requireContext())
        cardImagesRecycler.adapter = adapterImages

        colorIdentity.text = db.stringToSpannableString(combo.colorIdentity!!
            .uppercase()
            .replace(",", "")
            .replace("W", "{W}")
            .replace("U", "{U}")
            .replace("B", "{B}")
            .replace("R", "{R}")
            .replace("G", "{G}")
            .replace("C", "{C}"), colorIdentity.textSize.toInt())

        prerequisitesTextView.text = db.stringToSpannableString(combo.prerequisites!!, prerequisitesTextView.textSize.toInt())
        stepsTextView.text = db.stringToSpannableString(combo.steps!!, stepsTextView.textSize.toInt())
        resultsTextView.text = db.stringToSpannableString(combo.results!!, resultsTextView.textSize.toInt())
    }
}