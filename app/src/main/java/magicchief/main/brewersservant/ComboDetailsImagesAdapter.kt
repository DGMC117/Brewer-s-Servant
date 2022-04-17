package magicchief.main.brewersservant

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

class ComboDetailsImagesAdapter(val imageList: MutableList<String>, context: Context): RecyclerView.Adapter<ComboDetailsImagesAdapter.ViewHolder> () {
    val parentContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComboDetailsImagesAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_image_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ComboDetailsImagesAdapter.ViewHolder, position: Int) {
        Picasso.get().load(imageList[position]).placeholder(R.drawable.ic_baseline_image_not_supported_24).into(holder.itemImage)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    inner class ViewHolder (itemView: View): RecyclerView.ViewHolder (itemView) {
        var itemImage: ShapeableImageView

        init {
            itemImage = itemView.findViewById(R.id.row_combo_details_card_image)
        }
    }
}
