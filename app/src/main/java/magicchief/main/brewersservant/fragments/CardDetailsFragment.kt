package magicchief.main.brewersservant.fragments

import android.animation.Animator
import android.animation.AnimatorInflater
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.squareup.picasso.Picasso
import magicchief.main.brewersservant.*



class CardDetailsFragment : Fragment() {

    lateinit var cardId: String

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<CardDetailsSplitAdapter.ViewHolder>? = null

    private var layoutManagerRelated: RecyclerView.LayoutManager? = null
    private var relatedAdapter: RecyclerView.Adapter<CardDetailsRelatedAdapter.ViewHolder>? = null

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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var currentCardFace = 0

        val db = DBHelper (requireContext())
        val card = db.getCard(cardId)

        val cardImage = requireView().findViewById<ShapeableImageView>(R.id.card_details_image)
        val cardImage2 = requireView().findViewById<ShapeableImageView>(R.id.card_details_image2)
        if (card.image_uris != null && card.image_uris.art_crop != null && card.image_uris.art_crop?.toString() != "null") Picasso.get().load(card.image_uris.art_crop?.toString()).placeholder(R.drawable.ic_baseline_image_not_supported_24).into(cardImage)
        else {
            Picasso.get().load(card.card_faces?.get(0)?.image_uris?.art_crop?.toString()).placeholder(R.drawable.ic_baseline_image_not_supported_24).into(cardImage)
            Picasso.get().load(card.card_faces?.get(1)?.image_uris?.art_crop?.toString()).placeholder(R.drawable.ic_baseline_image_not_supported_24).into(cardImage2)
        }

        val flipFAB = requireView().findViewById<FloatingActionButton>(R.id.rotate_fab)

        val cardName = requireView().findViewById<TextView>(R.id.card_details_name)
        val cardManaCost = requireView().findViewById<TextView>(R.id.card_details_mana_cost)
        val cardType = requireView().findViewById<TextView>(R.id.card_details_type)
        val cardText = requireView().findViewById<TextView>(R.id.card_details_text)
        val cardPowTouLayout = requireView().findViewById<LinearLayout>(R.id.card_details_pow_tou_layout)
        val cardPower = requireView().findViewById<TextView>(R.id.card_details_power)
        val cardToughness = requireView().findViewById<TextView>(R.id.card_details_toughness)
        val cardLoyalty = requireView().findViewById<TextView>(R.id.card_details_loyalty)
        val cardFlavorText = requireView().findViewById<TextView>(R.id.card_details_flavor_text)
        val cardArtist = requireView().findViewById<TextView>(R.id.card_details_artist)

        var cardSplitList = requireView().findViewById<RecyclerView>(R.id.card_details_split_recycler)
        val noSplitLayout = requireView().findViewById<ConstraintLayout>(R.id.card_details_no_split_layout)

        val relatedLayout = requireView().findViewById<ConstraintLayout>(R.id.card_details_related_layout)
        val relatedText = requireView().findViewById<TextView>(R.id.card_details_related_text)
        var relatedList = requireView().findViewById<RecyclerView>(R.id.card_details_related_recycler)

        when (card.layout) {
            in listOf("normal", "leveler", "class", "saga", "planar", "scheme", "vanguard", "token", "emblem", "augment", "host", "meld") -> {
                cardName.text = card.name
                if (card.mana_cost != null && card.mana_cost != "") cardManaCost.text = db.stringToSpannableString(card.mana_cost!!, cardManaCost.textSize.toInt())
                else cardManaCost.text = ""
                if (card.type_line != null && card.type_line != "") cardType.text = card.type_line
                else cardType.text = ""
                if (card.oracle_text != null && card.oracle_text != "") cardText.text = db.stringToSpannableString(card.oracle_text!!, cardText.textSize.toInt())
                else cardText.visibility = View.GONE
                if (card.power != null && card.power != "") {
                    cardPowTouLayout.visibility = View.VISIBLE
                    cardPower.text = card.power
                    cardToughness.text = card.toughness
                }
                else cardPowTouLayout.visibility = View.GONE
                if (card.loyalty != null && card.loyalty != "") {
                    cardLoyalty.visibility = View.VISIBLE
                    cardLoyalty.text = card.loyalty
                }
                else cardLoyalty.visibility = View.GONE
                if (card.flavor_text != null && card.flavor_text != "") {
                    cardFlavorText.visibility = View.VISIBLE
                    cardFlavorText.text = "\"${card.flavor_text!!.replace("\"", "")}\""
                }
                else cardFlavorText.visibility = View.GONE
                if (card.artist != null && card.artist != "") {
                    cardArtist.visibility = View.VISIBLE
                    cardArtist.text = "\"${card.artist!!}\""
                }
                else cardArtist.visibility = View.GONE
            }
            in listOf("flip", "transform", "modal_dfc", "double_faced_token", "reversible_card", "art_series") -> {
                cardName.text = card.card_faces?.get(currentCardFace)?.name
                if (card.card_faces?.get(currentCardFace)?.mana_cost != null && card.card_faces?.get(currentCardFace)?.mana_cost != "") cardManaCost.text = db.stringToSpannableString(card.card_faces?.get(currentCardFace)?.mana_cost!!, cardManaCost.textSize.toInt())
                else cardManaCost.text = ""
                if (card.card_faces?.get(currentCardFace)?.type_line != null && card.card_faces?.get(currentCardFace)?.type_line != "") cardType.text = card.card_faces?.get(currentCardFace)?.type_line
                else cardType.text = ""
                if (card.card_faces?.get(currentCardFace)?.oracle_text != null && card.card_faces?.get(currentCardFace)?.oracle_text != "") {
                    cardText.visibility = View.VISIBLE
                    cardText.text = db.stringToSpannableString(card.card_faces?.get(currentCardFace)?.oracle_text!!, cardText.textSize.toInt())
                }
                else cardText.visibility = View.GONE
                if (card.card_faces?.get(currentCardFace)?.power != null && card.card_faces?.get(currentCardFace)?.power != "") {
                    cardPowTouLayout.visibility = View.VISIBLE
                    cardPower.text = card.card_faces?.get(currentCardFace)?.power
                    cardToughness.text = card.card_faces?.get(currentCardFace)?.toughness
                }
                else cardPowTouLayout.visibility = View.GONE
                if (card.card_faces?.get(currentCardFace)?.loyalty != null && card.card_faces?.get(currentCardFace)?.loyalty != "") {
                    cardLoyalty.visibility = View.VISIBLE
                    cardLoyalty.text = card.card_faces?.get(currentCardFace)?.loyalty
                }
                else cardLoyalty.visibility = View.GONE
                if (card.card_faces?.get(currentCardFace)?.flavor_text != null && card.card_faces?.get(currentCardFace)?.flavor_text != "") {
                    cardFlavorText.visibility = View.VISIBLE
                    cardFlavorText.text = "\"${card.card_faces?.get(currentCardFace)?.flavor_text!!.replace("\"", "")}\""
                }
                else cardFlavorText.visibility = View.GONE
                if (card.card_faces?.get(currentCardFace)?.artist != null && card.card_faces?.get(currentCardFace)?.artist != "") {
                    cardArtist.visibility = View.VISIBLE
                    cardArtist.text = "\"${card.card_faces?.get(currentCardFace)?.artist!!}\""
                }
                else cardArtist.visibility = View.GONE
                flipFAB.visibility = View.VISIBLE
                flipFAB.setOnClickListener {
                    if (card.layout == "flip") {
                        lateinit var animator:Animator
                        if (currentCardFace == 1) animator = AnimatorInflater.loadAnimator(requireContext(), R.animator.flip_back_animation)
                        else animator = AnimatorInflater.loadAnimator(requireContext(), R.animator.flip_animation)
                        animator.setTarget(cardImage)
                        animator.start()
                    }
                    else {
                        val animatorStart = AnimatorInflater.loadAnimator(requireContext(), R.animator.transform_animation_start)
                        val animatorEnd = AnimatorInflater.loadAnimator(requireContext(), R.animator.transform_animation_end)
                        if (currentCardFace == 0) {
                            animatorStart.setTarget(cardImage)
                            animatorEnd.setTarget(cardImage2)
                            animatorStart.doOnEnd {
                                cardImage.visibility = View.GONE
                                cardImage2.visibility = View.VISIBLE
                                animatorEnd.start()
                            }
                            animatorStart.start()
                        }
                        else {
                            animatorStart.setTarget(cardImage2)
                            animatorEnd.setTarget(cardImage)
                            animatorStart.doOnEnd {
                                cardImage.visibility = View.VISIBLE
                                cardImage2.visibility = View.GONE
                                animatorEnd.start()
                            }
                            animatorStart.start()
                        }
                    }
                    currentCardFace = if (currentCardFace == 0) 1 else 0
                    cardName.text = card.card_faces?.get(currentCardFace)?.name
                    if (card.card_faces?.get(currentCardFace)?.mana_cost != null && card.card_faces?.get(currentCardFace)?.mana_cost != "") cardManaCost.text = db.stringToSpannableString(card.card_faces?.get(currentCardFace)?.mana_cost!!, cardManaCost.textSize.toInt())
                    else cardManaCost.text = ""
                    if (card.card_faces?.get(currentCardFace)?.type_line != null && card.card_faces?.get(currentCardFace)?.type_line != "") cardType.text = card.card_faces?.get(currentCardFace)?.type_line
                    else cardType.text = ""
                    if (card.card_faces?.get(currentCardFace)?.oracle_text != null && card.card_faces?.get(currentCardFace)?.oracle_text != "") {
                        cardText.visibility = View.VISIBLE
                        cardText.text = db.stringToSpannableString(card.card_faces?.get(currentCardFace)?.oracle_text!!, cardText.textSize.toInt())
                    }
                    else cardText.visibility = View.GONE
                    if (card.card_faces?.get(currentCardFace)?.power != null && card.card_faces?.get(currentCardFace)?.power != "") {
                        cardPowTouLayout.visibility = View.VISIBLE
                        cardPower.text = card.card_faces?.get(currentCardFace)?.power
                        cardToughness.text = card.card_faces?.get(currentCardFace)?.toughness
                    }
                    else cardPowTouLayout.visibility = View.GONE
                    if (card.card_faces?.get(currentCardFace)?.loyalty != null && card.card_faces?.get(currentCardFace)?.loyalty != "") {
                        cardLoyalty.visibility = View.VISIBLE
                        cardLoyalty.text = card.card_faces?.get(currentCardFace)?.loyalty
                    }
                    else cardLoyalty.visibility = View.GONE
                    if (card.card_faces?.get(currentCardFace)?.flavor_text != null && card.card_faces?.get(currentCardFace)?.flavor_text != "") {
                        cardFlavorText.visibility = View.VISIBLE
                        cardFlavorText.text = "\"${card.card_faces?.get(currentCardFace)?.flavor_text!!.replace("\"", "")}\""
                    }
                    else cardFlavorText.visibility = View.GONE
                    if (card.card_faces?.get(currentCardFace)?.artist != null && card.card_faces?.get(currentCardFace)?.artist != "") {
                        cardArtist.visibility = View.VISIBLE
                        cardArtist.text = "\"${card.card_faces?.get(currentCardFace)?.artist!!}\""
                    }
                    else cardArtist.visibility = View.GONE
                }
            }
            in listOf("split", "adventure") -> {
                cardSplitList.visibility = View.VISIBLE
                noSplitLayout.visibility = View.GONE
                layoutManager = LinearLayoutManager (requireContext())
                cardSplitList.layoutManager = layoutManager
                adapter = CardDetailsSplitAdapter(card.card_faces!!, requireContext())
                cardSplitList.adapter = adapter
            }
        }

        val legalityStandard = requireView().findViewById<MaterialTextView>(R.id.standard_legality)
        when (card.legalities?.standard) {
            "legal" -> {
                legalityStandard.text = getString(R.string.legal)
                legalityStandard.background.setTint(resources.getColor(R.color.colorTriadic2, requireContext().theme))
            }
            "restricted" -> {
                legalityStandard.text = getString(R.string.restricted)
                legalityStandard.background.setTint(resources.getColor(R.color.colorTriadic1, requireContext().theme))
            }
            "banned" -> {
                legalityStandard.text = getString(R.string.banned)
                legalityStandard.background.setTint(resources.getColor(R.color.colorError, requireContext().theme))
            }
            else -> {
                legalityStandard.text = getString(R.string.not_legal)
                legalityStandard.background.setTint(resources.getColor(R.color.colorError, requireContext().theme))
            }
        }

        val legalityPioneer = requireView().findViewById<MaterialTextView>(R.id.pioneer_legality)
        when (card.legalities?.pioneer) {
            "legal" -> {
                legalityPioneer.text = getString(R.string.legal)
                legalityPioneer.background.setTint(resources.getColor(R.color.colorTriadic2, requireContext().theme))
            }
            "restricted" -> {
                legalityPioneer.text = getString(R.string.restricted)
                legalityPioneer.background.setTint(resources.getColor(R.color.colorTriadic1, requireContext().theme))
            }
            "banned" -> {
                legalityPioneer.text = getString(R.string.banned)
                legalityPioneer.background.setTint(resources.getColor(R.color.colorError, requireContext().theme))
            }
            else -> {
                legalityPioneer.text = getString(R.string.not_legal)
                legalityPioneer.background.setTint(resources.getColor(R.color.colorError, requireContext().theme))
            }
        }

        val legalityModern = requireView().findViewById<MaterialTextView>(R.id.modern_legality)
        when (card.legalities?.modern) {
            "legal" -> {
                legalityModern.text = getString(R.string.legal)
                legalityModern.background.setTint(resources.getColor(R.color.colorTriadic2, requireContext().theme))
            }
            "restricted" -> {
                legalityModern.text = getString(R.string.restricted)
                legalityModern.background.setTint(resources.getColor(R.color.colorTriadic1, requireContext().theme))
            }
            "banned" -> {
                legalityModern.text = getString(R.string.banned)
                legalityModern.background.setTint(resources.getColor(R.color.colorError, requireContext().theme))
            }
            else -> {
                legalityModern.text = getString(R.string.not_legal)
                legalityModern.background.setTint(resources.getColor(R.color.colorError, requireContext().theme))
            }
        }

        val legalityLegacy = requireView().findViewById<MaterialTextView>(R.id.legacy_legality)
        when (card.legalities?.legacy) {
            "legal" -> {
                legalityLegacy.text = getString(R.string.legal)
                legalityLegacy.background.setTint(resources.getColor(R.color.colorTriadic2, requireContext().theme))
            }
            "restricted" -> {
                legalityLegacy.text = getString(R.string.restricted)
                legalityLegacy.background.setTint(resources.getColor(R.color.colorTriadic1, requireContext().theme))
            }
            "banned" -> {
                legalityLegacy.text = getString(R.string.banned)
                legalityLegacy.background.setTint(resources.getColor(R.color.colorError, requireContext().theme))
            }
            else -> {
                legalityLegacy.text = getString(R.string.not_legal)
                legalityLegacy.background.setTint(resources.getColor(R.color.colorError, requireContext().theme))
            }
        }

        val legalityVintage = requireView().findViewById<MaterialTextView>(R.id.vintage_legality)
        when (card.legalities?.vintage) {
            "legal" -> {
                legalityVintage.text = getString(R.string.legal)
                legalityVintage.background.setTint(resources.getColor(R.color.colorTriadic2, requireContext().theme))
            }
            "restricted" -> {
                legalityVintage.text = getString(R.string.restricted)
                legalityVintage.background.setTint(resources.getColor(R.color.colorTriadic1, requireContext().theme))
            }
            "banned" -> {
                legalityVintage.text = getString(R.string.banned)
                legalityVintage.background.setTint(resources.getColor(R.color.colorError, requireContext().theme))
            }
            else -> {
                legalityVintage.text = getString(R.string.not_legal)
                legalityVintage.background.setTint(resources.getColor(R.color.colorError, requireContext().theme))
            }
        }

        val legalityBrawl = requireView().findViewById<MaterialTextView>(R.id.brawl_legality)
        when (card.legalities?.brawl) {
            "legal" -> {
                legalityBrawl.text = getString(R.string.legal)
                legalityBrawl.background.setTint(resources.getColor(R.color.colorTriadic2, requireContext().theme))
            }
            "restricted" -> {
                legalityBrawl.text = getString(R.string.restricted)
                legalityBrawl.background.setTint(resources.getColor(R.color.colorTriadic1, requireContext().theme))
            }
            "banned" -> {
                legalityBrawl.text = getString(R.string.banned)
                legalityBrawl.background.setTint(resources.getColor(R.color.colorError, requireContext().theme))
            }
            else -> {
                legalityBrawl.text = getString(R.string.not_legal)
                legalityBrawl.background.setTint(resources.getColor(R.color.colorError, requireContext().theme))
            }
        }

        val legalityHistoric = requireView().findViewById<MaterialTextView>(R.id.historic_legality)
        when (card.legalities?.historic) {
            "legal" -> {
                legalityHistoric.text = getString(R.string.legal)
                legalityHistoric.background.setTint(resources.getColor(R.color.colorTriadic2, requireContext().theme))
            }
            "restricted" -> {
                legalityHistoric.text = getString(R.string.restricted)
                legalityHistoric.background.setTint(resources.getColor(R.color.colorTriadic1, requireContext().theme))
            }
            "banned" -> {
                legalityHistoric.text = getString(R.string.banned)
                legalityHistoric.background.setTint(resources.getColor(R.color.colorError, requireContext().theme))
            }
            else -> {
                legalityHistoric.text = getString(R.string.not_legal)
                legalityHistoric.background.setTint(resources.getColor(R.color.colorError, requireContext().theme))
            }
        }

        val legalityPauper = requireView().findViewById<MaterialTextView>(R.id.pauper_legality)
        when (card.legalities?.pauper) {
            "legal" -> {
                legalityPauper.text = getString(R.string.legal)
                legalityPauper.background.setTint(resources.getColor(R.color.colorTriadic2, requireContext().theme))
            }
            "restricted" -> {
                legalityPauper.text = getString(R.string.restricted)
                legalityPauper.background.setTint(resources.getColor(R.color.colorTriadic1, requireContext().theme))
            }
            "banned" -> {
                legalityPauper.text = getString(R.string.banned)
                legalityPauper.background.setTint(resources.getColor(R.color.colorError, requireContext().theme))
            }
            else -> {
                legalityPauper.text = getString(R.string.not_legal)
                legalityPauper.background.setTint(resources.getColor(R.color.colorError, requireContext().theme))
            }
        }

        val legalityPenny = requireView().findViewById<MaterialTextView>(R.id.penny_legality)
        when (card.legalities?.penny) {
            "legal" -> {
                legalityPenny.text = getString(R.string.legal)
                legalityPenny.background.setTint(resources.getColor(R.color.colorTriadic2, requireContext().theme))
            }
            "restricted" -> {
                legalityPenny.text = getString(R.string.restricted)
                legalityPenny.background.setTint(resources.getColor(R.color.colorTriadic1, requireContext().theme))
            }
            "banned" -> {
                legalityPenny.text = getString(R.string.banned)
                legalityPenny.background.setTint(resources.getColor(R.color.colorError, requireContext().theme))
            }
            else -> {
                legalityPenny.text = getString(R.string.not_legal)
                legalityPenny.background.setTint(resources.getColor(R.color.colorError, requireContext().theme))
            }
        }

        val legalityCommander = requireView().findViewById<MaterialTextView>(R.id.commander_legality)
        when (card.legalities?.commander) {
            "legal" -> {
                legalityCommander.text = getString(R.string.legal)
                legalityCommander.background.setTint(resources.getColor(R.color.colorTriadic2, requireContext().theme))
            }
            "restricted" -> {
                legalityCommander.text = getString(R.string.restricted)
                legalityCommander.background.setTint(resources.getColor(R.color.colorTriadic1, requireContext().theme))
            }
            "banned" -> {
                legalityCommander.text = getString(R.string.banned)
                legalityCommander.background.setTint(resources.getColor(R.color.colorError, requireContext().theme))
            }
            else -> {
                legalityCommander.text = getString(R.string.not_legal)
                legalityCommander.background.setTint(resources.getColor(R.color.colorError, requireContext().theme))
            }
        }

        if (!card.all_parts.isNullOrEmpty()) {
            relatedLayout.visibility = View.VISIBLE
            layoutManagerRelated = LinearLayoutManager (requireContext())
            relatedList.layoutManager = layoutManagerRelated
            relatedAdapter = CardDetailsRelatedAdapter(card.all_parts!!, requireContext(), card.name!!)
            relatedList.adapter = relatedAdapter
        }
    }
}