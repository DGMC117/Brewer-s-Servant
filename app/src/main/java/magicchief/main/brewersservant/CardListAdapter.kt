package magicchief.main.brewersservant

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import magicchief.main.brewersservant.dataclass.Card

class CardListAdapter(val cardList: MutableList<Card>): RecyclerView.Adapter<CardListAdapter.ViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_list_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTitle.text = cardList[position].name
        holder.itemDetail.text = cardList[position].mana_cost

        if (cardList[position].image_uris != null && cardList[position].image_uris?.art_crop != null) Picasso.get().load(cardList[position].image_uris?.art_crop?.toString()).into(holder.itemImage)
        else holder.itemImage.setImageResource(R.drawable.ic_baseline_image_not_supported_24)
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    inner class ViewHolder (itemView: View): RecyclerView.ViewHolder (itemView) {
        var itemImage: ImageView
        var itemTitle: TextView
        var itemDetail: TextView

        init {
            itemImage = itemView.findViewById(R.id.card_image)
            itemTitle = itemView.findViewById(R.id.card_title)
            itemDetail= itemView.findViewById(R.id.card_text)
        }
    }

}