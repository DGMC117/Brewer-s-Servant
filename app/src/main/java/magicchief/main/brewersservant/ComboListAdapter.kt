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
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import magicchief.main.brewersservant.dataclass.Combo
import magicchief.main.brewersservant.fragments.CardListFragmentDirections
import magicchief.main.brewersservant.fragments.ComboListFragmentDirections

class ComboListAdapter(val comboList: MutableList<Combo>, context: Context): RecyclerView.Adapter<ComboListAdapter.ViewHolder> () {
    val parentContext = context

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<ComboRowCardNameAdapter.ViewHolder>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComboListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.combo_list_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ComboListAdapter.ViewHolder, position: Int) {
        layoutManager = LinearLayoutManager (parentContext)
        holder.itemNameRecyclerView.layoutManager = layoutManager
        adapter = ComboRowCardNameAdapter(comboList[position].cards!!, parentContext)
        holder.itemNameRecyclerView.adapter = adapter
        holder.itemColorIdentity.text = stringToSpannableString(comboList[position].colorIdentity!!, holder.itemColorIdentity.textSize.toInt(), true)
        holder.itemResultText.text = stringToSpannableString(comboList[position].results!!, holder.itemResultText.textSize.toInt(), false)
        holder.itemView.setOnClickListener {
            val action = ComboListFragmentDirections.actionComboListFragmentToComboDetailsFragment(comboId = comboList[position].id!!)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return comboList.size
    }

    inner class ViewHolder (itemView: View): RecyclerView.ViewHolder (itemView) {
        var itemNameRecyclerView: RecyclerView
        var itemColorIdentity: TextView
        var itemResultText: TextView

        init {
            itemNameRecyclerView = itemView.findViewById(R.id.row_combo_card_name_list)
            itemColorIdentity = itemView.findViewById(R.id.row_combo_color_identity)
            itemResultText = itemView.findViewById(R.id.row_combo_result_text)
        }
    }

    fun stringToSpannableString (string: String, textSize: Int, isColorIdentity: Boolean): SpannableString {
        var str = string
        if (isColorIdentity) {
            str = str.uppercase()
            str = str.replace(",", "")
            str = str.replace("W", "{W}")
            str = str.replace("U", "{U}")
            str = str.replace("B", "{B}")
            str = str.replace("R", "{R}")
            str = str.replace("G", "{G}")
            str = str.replace("C", "{C}")
        }
        var result = SpannableString (str)
        while (str.indexOf('{') != -1) {
            val subStart = str.indexOf('{')
            val subEnd = str.indexOf('}')
            val subString = str.substring(subStart, subEnd + 1)
            val draw = getSymbolDrawable(subString.uppercase())
            val proportion = draw.intrinsicWidth / draw.intrinsicHeight
            draw.setBounds(0, 0, textSize * proportion, textSize)
            val imageSpan = ImageSpan (draw, ImageSpan.ALIGN_BOTTOM)
            result.setSpan(imageSpan, subStart, subEnd + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            str = str.replaceFirst('{', '0')
            str = str.replaceFirst('}', '0')
        }
        return result
    }

    fun getSymbolDrawable (str: String): Drawable {
        when (str) {
            "{T}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_t_cost)!!
            "{Q}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_q_cost)!!
            "{E}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_e_cost)!!
            "{PW}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_pw_cost)!!
            "{CHAOS}" -> return AppCompatResources.getDrawable(
                parentContext,
                R.drawable.ic_chaos_cost
            )!!
            "{A}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_a_cost)!!
            "{X}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_x_cost)!!
            "{Y}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_y_cost)!!
            "{Z}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_z_cost)!!
            "{0}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_0_cost)!!
            "{1}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_1_cost)!!
            "{2}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_2_cost)!!
            "{3}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_3_cost)!!
            "{4}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_4_cost)!!
            "{5}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_5_cost)!!
            "{6}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_6_cost)!!
            "{7}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_7_cost)!!
            "{8}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_8_cost)!!
            "{9}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_9_cost)!!
            "{10}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_10_cost)!!
            "{11}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_11_cost)!!
            "{12}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_12_cost)!!
            "{13}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_13_cost)!!
            "{14}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_14_cost)!!
            "{15}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_15_cost)!!
            "{16}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_16_cost)!!
            "{17}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_17_cost)!!
            "{18}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_18_cost)!!
            "{19}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_19_cost)!!
            "{20}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_20_cost)!!
            "{100}" -> return AppCompatResources.getDrawable(
                parentContext,
                R.drawable.ic_100_cost
            )!!
            "{1000000}" -> return AppCompatResources.getDrawable(
                parentContext,
                R.drawable.ic_1000000_cost
            )!!
            "{∞}" -> return AppCompatResources.getDrawable(
                parentContext,
                R.drawable.ic_infinity_cost
            )!!
            "{W/U}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_wu_cost)!!
            "{W/B}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_wb_cost)!!
            "{B/R}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_br_cost)!!
            "{B/G}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_bg_cost)!!
            "{U/B}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_ub_cost)!!
            "{U/R}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_ur_cost)!!
            "{R/G}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_rg_cost)!!
            "{R/W}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_rw_cost)!!
            "{G/W}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_gw_cost)!!
            "{G/U}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_gu_cost)!!
            "{W/U/P}" -> return AppCompatResources.getDrawable(
                parentContext,
                R.drawable.ic_wup_cost
            )!!
            "{W/B/P}" -> return AppCompatResources.getDrawable(
                parentContext,
                R.drawable.ic_wbp_cost
            )!!
            "{B/R/P}" -> return AppCompatResources.getDrawable(
                parentContext,
                R.drawable.ic_brp_cost
            )!!
            "{B/G/P}" -> return AppCompatResources.getDrawable(
                parentContext,
                R.drawable.ic_bgp_cost
            )!!
            "{U/B/P}" -> return AppCompatResources.getDrawable(
                parentContext,
                R.drawable.ic_ubp_cost
            )!!
            "{U/R/P}" -> return AppCompatResources.getDrawable(
                parentContext,
                R.drawable.ic_urp_cost
            )!!
            "{R/G/P}" -> return AppCompatResources.getDrawable(
                parentContext,
                R.drawable.ic_rgp_cost
            )!!
            "{R/W/P}" -> return AppCompatResources.getDrawable(
                parentContext,
                R.drawable.ic_rwp_cost
            )!!
            "{G/W/P}" -> return AppCompatResources.getDrawable(
                parentContext,
                R.drawable.ic_gwp_cost
            )!!
            "{G/U/P}" -> return AppCompatResources.getDrawable(
                parentContext,
                R.drawable.ic_gup_cost
            )!!
            "{2/W}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_2w_cost)!!
            "{2/U}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_2u_cost)!!
            "{2/B}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_2b_cost)!!
            "{2/R}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_2r_cost)!!
            "{2/G}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_2g_cost)!!
            "{W}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_w_cost)!!
            "{U}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_u_cost)!!
            "{B}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_b_cost)!!
            "{R}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_r_cost)!!
            "{G}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_g_cost)!!
            "{P}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_p_cost)!!
            "{W/P}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_wp_cost)!!
            "{U/P}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_up_cost)!!
            "{B/P}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_bp_cost)!!
            "{R/P}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_rp_cost)!!
            "{G/P}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_gp_cost)!!
            "{C}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_c_cost)!!
            "{S}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_s_cost)!!
            "{HW}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_hw_cost)!!
            "{HR}" -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_hr_cost)!!
            else -> return AppCompatResources.getDrawable(parentContext, R.drawable.ic_1_2_cost)!!
        }
    }
}