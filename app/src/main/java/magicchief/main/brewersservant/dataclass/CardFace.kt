package magicchief.main.brewersservant.dataclass

import java.util.*

class CardFace (
    val artist: String,
    val color_indicator: Array<String>,
    val colors: Array<String>,
    val flavor_text: String,
    val illustration_id: UUID,
    val image_uris: ImageURIs,
    val layout: String,
    val loyalty: String,
    val mana_cost: String,
    val name: String,
    val oracle_text: String,
    val power: String,
    val printed_name: String,
    val printed_text: String,
    val printed_type_line: String,
    val toughness: String,
    val type_line: String,
    val watermark: String
        )