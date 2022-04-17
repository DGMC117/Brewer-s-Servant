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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.get
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import magicchief.main.brewersservant.dataclass.Card
import magicchief.main.brewersservant.dataclass.Deck
import magicchief.main.brewersservant.fragments.CardListFragmentDirections
import magicchief.main.brewersservant.fragments.CardSearchFragmentDirections
import magicchief.main.brewersservant.fragments.DecksListFragment
import magicchief.main.brewersservant.fragments.DecksListFragmentDirections

class DecksListAdapter(val decksList: MutableList<Deck>, context: Context): RecyclerView.Adapter<DecksListAdapter.ViewHolder> () {
    val parentContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.decks_list_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (decksList[position].face_card_image_uri != null) Picasso.get().load(decksList[position].face_card_image_uri).placeholder(R.drawable.ic_baseline_image_not_supported_24).into(holder.itemImage)
        holder.itemName.text = decksList[position].name
        holder.itemFormat.text = decksList[position].format
        holder.itemColorIdentity.text = stringToSpannableString(decksList[position].colorIdentity!!, holder.itemColorIdentity.textSize.toInt(), true)
        val db = DBHelper(parentContext)
        holder.itemDeleteButton.setOnClickListener {
            MaterialAlertDialogBuilder(parentContext)
                .setTitle(parentContext.getString(R.string.delete_deck_alert_title) + " ${holder.itemName.text}?")
                .setMessage(parentContext.getString(R.string.delete_deck_alert_text))
                .setNegativeButton(parentContext.getString(R.string.cancel)) { dialog, which ->
                    // Respond to negative button press
                }
                .setPositiveButton(parentContext.getString(R.string.accept)) { dialog, which ->
                    db.deleteDeck(decksList[position].id!!)
                }
                .show()
        }
        holder.itemView.setOnClickListener {
            val action = DecksListFragmentDirections.actionDecksListFragmentToDeckFragment(deckId = decksList[position].id!!)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return decksList.size
    }

    inner class ViewHolder (itemView: View): RecyclerView.ViewHolder (itemView) {
        var itemImage: ShapeableImageView
        var itemName: TextView
        var itemFormat: TextView
        var itemColorIdentity: TextView
        var itemDeleteButton: MaterialButton

        init {
            itemImage = itemView.findViewById(R.id.deck_list_row_image)
            itemName = itemView.findViewById(R.id.deck_list_row_name)
            itemFormat = itemView.findViewById(R.id.deck_list_row_format)
            itemColorIdentity = itemView.findViewById(R.id.deck_list_row_color_identity)
            itemDeleteButton= itemView.findViewById(R.id.deck_list_row_delete_button)
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