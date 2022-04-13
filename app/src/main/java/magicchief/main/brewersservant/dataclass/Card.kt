package magicchief.main.brewersservant.dataclass

import java.net.URI
import java.util.*

class Card (
    // Core Card Fields
    var id: UUID? = null,

    // Gameplay Fields
    var all_parts: Array<RelatedCard>? = null,
    var card_faces: Array<CardFace>? = null,
    var cmc: Double? = null,
    var color_identity: Array<String>? = null,
    var colors: Array<String>? = null,
    var layout: String? = null, // normal, split, flip, transform, modal_dfc, meld, leveler, class, saga, adventure, planar, scheme, vanguard, token, double_faced_token, emblem, augment, host, art_series, double_sided
    val legalities: Legalities? = Legalities(),
    var loyalty: String? = null,
    var mana_cost: String? = null,
    var name: String? = null,
    var oracle_text: String? = null,
    var power: String? = null,
    var produced_mana: Array<String>? = null,
    var toughness: String? = null,
    var type_line: String? = null,

    // Print Fields
    var artist: String? = null,
    var flavor_text: String? = null,
    val image_uris: ImageURIs? = ImageURIs(),
    val prices: Prices? = Prices(),
    var rarity: String? = null, // common, uncommon, rare, special, mythic, bonus
    val set: String? = null,
    var set_id: String? = null,
    // Preview data not used
        )