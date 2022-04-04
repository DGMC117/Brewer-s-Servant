package magicchief.main.brewersservant

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import magicchief.main.brewersservant.dataclass.Card

class CardListAdapter(val cardList: MutableList<Card>, context: Context): RecyclerView.Adapter<CardListAdapter.ViewHolder> () {
    val parentContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_list_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTitle.text = cardList[position].name
        holder.itemDetail.text = stringToSpannableString (cardList[position].mana_cost.toString(), holder.itemDetail.textSize.toInt())

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

    fun stringToSpannableString (string: String, textSize: Int): SpannableString {
        var str = string
        var result = SpannableString (str)
        while (str.indexOf('{') != -1) {
            val subStart = str.indexOf('{')
            val subEnd = str.indexOf('}')
            val subString = str.substring(subStart, subEnd + 1)
            val draw = getSymbolDrawable(subString)
            val proportion = draw.intrinsicWidth / draw.intrinsicHeight
            draw.setBounds(0, 0, textSize * proportion, textSize)
            val imageSpan = ImageSpan (draw, ImageSpan.ALIGN_BASELINE)
            result.setSpan(imageSpan, subStart, subEnd + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            str = str.replaceFirst('{', '0')
            str = str.replaceFirst('}', '0')
        }
        return result
    }

    fun getSymbolDrawable (str: String): Drawable {
        when (str) {
            "{T}" -> return getDrawable(parentContext, R.drawable.ic_t_cost)!!
            "{Q}" -> return getDrawable(parentContext, R.drawable.ic_q_cost)!!
            "{E}" -> return getDrawable(parentContext, R.drawable.ic_e_cost)!!
            "{PW}" -> return getDrawable(parentContext, R.drawable.ic_pw_cost)!!
            "{CHAOS}" -> return getDrawable(parentContext, R.drawable.ic_chaos_cost)!!
            "{A}" -> return getDrawable(parentContext, R.drawable.ic_a_cost)!!
            "{X}" -> return getDrawable(parentContext, R.drawable.ic_x_cost)!!
            "{Y}" -> return getDrawable(parentContext, R.drawable.ic_y_cost)!!
            "{Z}" -> return getDrawable(parentContext, R.drawable.ic_z_cost)!!
            "{0}" -> return getDrawable(parentContext, R.drawable.ic_0_cost)!!
            "{1}" -> return getDrawable(parentContext, R.drawable.ic_1_cost)!!
            "{2}" -> return getDrawable(parentContext, R.drawable.ic_2_cost)!!
            "{3}" -> return getDrawable(parentContext, R.drawable.ic_3_cost)!!
            "{4}" -> return getDrawable(parentContext, R.drawable.ic_4_cost)!!
            "{5}" -> return getDrawable(parentContext, R.drawable.ic_5_cost)!!
            "{6}" -> return getDrawable(parentContext, R.drawable.ic_6_cost)!!
            "{7}" -> return getDrawable(parentContext, R.drawable.ic_7_cost)!!
            "{8}" -> return getDrawable(parentContext, R.drawable.ic_8_cost)!!
            "{9}" -> return getDrawable(parentContext, R.drawable.ic_9_cost)!!
            "{10}" -> return getDrawable(parentContext, R.drawable.ic_10_cost)!!
            "{11}" -> return getDrawable(parentContext, R.drawable.ic_11_cost)!!
            "{12}" -> return getDrawable(parentContext, R.drawable.ic_12_cost)!!
            "{13}" -> return getDrawable(parentContext, R.drawable.ic_13_cost)!!
            "{14}" -> return getDrawable(parentContext, R.drawable.ic_14_cost)!!
            "{15}" -> return getDrawable(parentContext, R.drawable.ic_15_cost)!!
            "{16}" -> return getDrawable(parentContext, R.drawable.ic_16_cost)!!
            "{17}" -> return getDrawable(parentContext, R.drawable.ic_17_cost)!!
            "{18}" -> return getDrawable(parentContext, R.drawable.ic_18_cost)!!
            "{19}" -> return getDrawable(parentContext, R.drawable.ic_19_cost)!!
            "{20}" -> return getDrawable(parentContext, R.drawable.ic_20_cost)!!
            "{100}" -> return getDrawable(parentContext, R.drawable.ic_100_cost)!!
            "{1000000}" -> return getDrawable(parentContext, R.drawable.ic_1000000_cost)!!
            "{∞}" -> return getDrawable(parentContext, R.drawable.ic_infinity_cost)!!
            "{W/U}" -> return getDrawable(parentContext, R.drawable.ic_wu_cost)!!
            "{W/B}" -> return getDrawable(parentContext, R.drawable.ic_wb_cost)!!
            "{B/R}" -> return getDrawable(parentContext, R.drawable.ic_br_cost)!!
            "{B/G}" -> return getDrawable(parentContext, R.drawable.ic_bg_cost)!!
            "{U/B}" -> return getDrawable(parentContext, R.drawable.ic_ub_cost)!!
            "{U/R}" -> return getDrawable(parentContext, R.drawable.ic_ur_cost)!!
            "{R/G}" -> return getDrawable(parentContext, R.drawable.ic_rg_cost)!!
            "{R/W}" -> return getDrawable(parentContext, R.drawable.ic_rw_cost)!!
            "{G/W}" -> return getDrawable(parentContext, R.drawable.ic_gw_cost)!!
            "{G/U}" -> return getDrawable(parentContext, R.drawable.ic_gu_cost)!!
            "{W/U/P}" -> return getDrawable(parentContext, R.drawable.ic_wup_cost)!!
            "{W/B/P}" -> return getDrawable(parentContext, R.drawable.ic_wbp_cost)!!
            "{B/R/P}" -> return getDrawable(parentContext, R.drawable.ic_brp_cost)!!
            "{B/G/P}" -> return getDrawable(parentContext, R.drawable.ic_bgp_cost)!!
            "{U/B/P}" -> return getDrawable(parentContext, R.drawable.ic_ubp_cost)!!
            "{U/R/P}" -> return getDrawable(parentContext, R.drawable.ic_urp_cost)!!
            "{R/G/P}" -> return getDrawable(parentContext, R.drawable.ic_rgp_cost)!!
            "{R/W/P}" -> return getDrawable(parentContext, R.drawable.ic_rwp_cost)!!
            "{G/W/P}" -> return getDrawable(parentContext, R.drawable.ic_gwp_cost)!!
            "{G/U/P}" -> return getDrawable(parentContext, R.drawable.ic_gup_cost)!!
            "{2/W}" -> return getDrawable(parentContext, R.drawable.ic_2w_cost)!!
            "{2/U}" -> return getDrawable(parentContext, R.drawable.ic_2u_cost)!!
            "{2/B}" -> return getDrawable(parentContext, R.drawable.ic_2b_cost)!!
            "{2/R}" -> return getDrawable(parentContext, R.drawable.ic_2r_cost)!!
            "{2/G}" -> return getDrawable(parentContext, R.drawable.ic_2g_cost)!!
            "{W}" -> return getDrawable(parentContext, R.drawable.ic_w_cost)!!
            "{U}" -> return getDrawable(parentContext, R.drawable.ic_u_cost)!!
            "{B}" -> return getDrawable(parentContext, R.drawable.ic_b_cost)!!
            "{R}" -> return getDrawable(parentContext, R.drawable.ic_r_cost)!!
            "{G}" -> return getDrawable(parentContext, R.drawable.ic_g_cost)!!
            "{P}" -> return getDrawable(parentContext, R.drawable.ic_p_cost)!!
            "{W/P}" -> return getDrawable(parentContext, R.drawable.ic_wp_cost)!!
            "{U/P}" -> return getDrawable(parentContext, R.drawable.ic_up_cost)!!
            "{B/P}" -> return getDrawable(parentContext, R.drawable.ic_bp_cost)!!
            "{R/P}" -> return getDrawable(parentContext, R.drawable.ic_rp_cost)!!
            "{G/P}" -> return getDrawable(parentContext, R.drawable.ic_gp_cost)!!
            "{C}" -> return getDrawable(parentContext, R.drawable.ic_c_cost)!!
            "{S}" -> return getDrawable(parentContext, R.drawable.ic_s_cost)!!
            "{HW}" -> return getDrawable(parentContext, R.drawable.ic_hw_cost)!!
            "{HR}" -> return getDrawable(parentContext, R.drawable.ic_hr_cost)!!
            else -> return getDrawable(parentContext, R.drawable.ic_1_2_cost)!!
        }
    }

}