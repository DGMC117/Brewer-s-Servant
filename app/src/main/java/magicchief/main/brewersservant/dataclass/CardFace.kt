package magicchief.main.brewersservant.dataclass

import java.util.*

class CardFace (
    val artist: String,
    val cmc: Double,
    val colors: Array<String>,
    val flavor_text: String,
    val image_uris: ImageURIs,
    val layout: String,
    val loyalty: String,
    val mana_cost: String,
    val name: String,
    val oracle_text: String,
    val power: String,
    val toughness: String,
    val type_line: String
        )