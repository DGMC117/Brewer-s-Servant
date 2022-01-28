package magicchief.main.brewersservant.dataclass

import java.net.URI
import java.util.*

class Set (
    val id: UUID,
    val code: String,
    val mtgo_code: String,
    val tcgplayer_id: Int,
    val name: String,
    val set_type: String,
    val released_at: Date,
    val block_code: String,
    val block: String,
    val parent_set_code: String,
    val card_count: Int,
    val printed_size: Int,
    val digital: Boolean,
    val foil_only: Boolean,
    val nonfoil_only: Boolean,
    val scryfall_uri: URI,
    val uri: URI,
    val icon_svg_uri: URI,
    val search_uri: URI
    )