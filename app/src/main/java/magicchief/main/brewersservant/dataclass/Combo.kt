package magicchief.main.brewersservant.dataclass

data class Combo (
    var id: Int? = null,
    var cards: Array<String>? = null,
    var colorIdentity: String? = null,
    var prerequisites: String? = null,
    var steps: String? = null,
    var results: String? = null
        )