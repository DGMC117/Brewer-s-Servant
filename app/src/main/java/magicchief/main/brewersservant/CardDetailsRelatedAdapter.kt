package magicchief.main.brewersservant

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.get
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.squareup.picasso.Picasso
import magicchief.main.brewersservant.dataclass.Card
import magicchief.main.brewersservant.dataclass.CardFace
import magicchief.main.brewersservant.dataclass.RelatedCard
import magicchief.main.brewersservant.fragments.CardListFragmentDirections
import magicchief.main.brewersservant.fragments.CardSearchFragmentDirections

class CardDetailsRelatedAdapter(val relatedCards: Array<RelatedCard>, context: Context, val cardName: String): RecyclerView.Adapter<CardDetailsRelatedAdapter.ViewHolder> () {
    val parentContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.details_related_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemName.text = relatedCards[position].name
        when (relatedCards[position].component) {
            "token" -> holder.itemComponent.text = parentContext.getString(R.string.token)
            "meld_part" -> holder.itemComponent.text = parentContext.getString(R.string.meld_part)
            "meld_result" -> holder.itemComponent.text = parentContext.getString(R.string.meld_result)
            "combo_piece" -> holder.itemComponent.text = parentContext.getString(R.string.combo_piece)
        }
    }

    override fun getItemCount(): Int {
        return relatedCards.size
    }

    inner class ViewHolder (itemView: View): RecyclerView.ViewHolder (itemView) {
        var itemName: TextView
        var itemComponent: TextView

        init {
            itemName = itemView.findViewById(R.id.card_details_related_row_name)
            itemComponent = itemView.findViewById(R.id.card_details_related_row_component)
        }
    }

}