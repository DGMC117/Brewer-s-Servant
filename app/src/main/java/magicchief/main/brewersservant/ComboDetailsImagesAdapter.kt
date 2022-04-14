package magicchief.main.brewersservant

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import magicchief.main.brewersservant.dataclass.Combo

class ComboDetailsImagesAdapter(val imageList: MutableList<String>, context: Context): RecyclerView.Adapter<ComboDetailsImagesAdapter.ViewHolder> () {
    val parentContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComboDetailsImagesAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_image_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ComboDetailsImagesAdapter.ViewHolder, position: Int) {
        Picasso.get().load(imageList[position]).into(holder.itemImage)
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