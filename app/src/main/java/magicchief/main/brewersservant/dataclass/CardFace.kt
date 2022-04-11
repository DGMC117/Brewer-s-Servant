package magicchief.main.brewersservant.dataclass

import java.util.*

class CardFace (
    var artist: String? = null,
    var cmc: Double? = null,
    var colors: Array<String>? = null,
    var flavor_text: String? = null,
    var image_uris: ImageURIs? = ImageURIs(),
    var layout: String? = null,
    var loyalty: String? = null,
    var mana_cost: String? = null,
    var name: String? = null,
    var oracle_text: String? = null,
    var power: String? = null,
    var toughness: String? = null,
    var type_line: String? = null
        )