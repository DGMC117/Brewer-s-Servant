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
import magicchief.main.brewersservant.dataclass.Combo

class ComboRowCardNameAdapter(val nameList: Array<String>, context: Context): RecyclerView.Adapter<ComboRowCardNameAdapter.ViewHolder> () {
    val parentContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComboRowCardNameAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_name_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ComboRowCardNameAdapter.ViewHolder, position: Int) {
        holder.itemName.text = nameList[position]
    }

    override fun getItemCount(): Int {
        return nameList.size
    }

    inner class ViewHolder (itemView: View): RecyclerView.ViewHolder (itemView) {
        var itemName: TextView

        init {
            itemName = itemView.findViewById(R.id.row_combo_card_name)
        }
    }
}