package magicchief.main.brewersservant.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import magicchief.main.brewersservant.CardListAdapter
import magicchief.main.brewersservant.DBHelper
import magicchief.main.brewersservant.R

lateinit var cardId: String

class CardDetailsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cardId = it.getString("cardId").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = DBHelper (requireContext())
        val card = db.getCard(cardId)

        val cardImage = requireView().findViewById<ShapeableImageView>(R.id.card_details_image)
        if (card.image_uris != null && card.image_uris?.art_crop != null && card.image_uris?.art_crop?.toString() != "null") Picasso.get().load(card.image_uris?.art_crop?.toString()).into(cardImage)
        else Picasso.get().load(card.card_faces?.get(0)?.image_uris?.art_crop?.toString()).into(cardImage)
    }
}