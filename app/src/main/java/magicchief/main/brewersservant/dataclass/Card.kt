package magicchief.main.brewersservant.dataclass

import java.net.URI
import java.util.*

class Card (
    // Core Card Fields
    val arena_id: Int,
    val id: UUID,
    val lang: String,
    val mtgo_id: Int,
    val mtgo_foil_id: Int,
    val multiverse_ids: Array<Int>,
    val tcgplayer_id: Int,
    val tcgplayer_etched_id: Int,
    val cardmarket_id: Int,
    val oracle_id: UUID,
    val prints_search_uri: URI,
    val rulings_uri: URI,
    val scryfall_uri: URI,
    val uri: URI,

    // Gameplay Fields
    val all_parts: Array<RelatedCard>,
    val card_faces: Array<CardFace>,
    val cmc: Double,
    val color_identity: Array<String>,
    val color_indicator: Array<String>,
    val colors: Array<String>,
    val edhrec_rank: Int,
    val hand_modifier: String,
    val keywords: Array<String>,
    val layout: String, // normal, split, flip, transform, modal_dfc, meld, leveler, class, saga, adventure, planar, scheme, vanguard, token, double_faced_token, emblem, augment, host, art_series, double_sided
    val legalities: Legalities,
    val life_modifier: String,
    val loyalty: String,
    val mana_cost: String,
    val name: String,
    val oracle_text: String,
    val oversized: Boolean,
    val power: String,
    val produced_mana: Array<String>,
    val reserved: Boolean,
    val toughness: String,
    val type_line: String,

    // Print Fields
    val artist: String,
    val booster: Boolean,
    val border_color: String,
    val card_back_id: UUID,
    val collector_number: String,
    val content_warning: Boolean,
    val digital: Boolean,
    val finishes: Array<String>,    // foil, nonfoil, etched, glossy
    val flavor_name: String,
    val flavor_text: String,
    val frame_effects: Array<String>,   // legendary, miracle, nyxtouched, draft, devoid, tombstone, colorshifted, inverted, sunmoondfc, compasslanddfc, originpwdfc, mooneldrazidfc, waxingandwaningmoondfc, showcase, extendedart, companion, etched, snow
    val frame: String,  // 1993, 1997, 2003, 2015, future
    val full_art: Boolean,
    val games: Array<String>,   // paper, arena, mtgo
    val highres_image: Boolean,
    val illustration_id: UUID,
    val image_status: String,
    val image_uris: ImageURIs,
    val prices: Prices,
    val printed_name: String,
    val printed_text: String,
    val printed_type_line: String,
    val promo: Boolean,
    val promo_types: Array<String>, //
    val purchase_uris: PurchaseURIs,
    val rarity: String, // common, uncommon, rare, special, mythic, bonus
    val related_uris: RelatedURIs,
    val released_at: Date,
    val reprint: Boolean,
    val scryfall_set_uri: URI,
    val set_name: String,
    val set_search_uri: URI,
    val set_type: String,
    val set_uri: URI,
    val set: String,
    val set_id: String,
    val story_spotlight: Boolean,
    val textless: Boolean,
    val variation: Boolean,
    val variation_of: UUID,
    val watermark: String,
    // Preview data not used
        )