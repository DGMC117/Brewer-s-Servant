package magicchief.main.brewersservant.dataclass

data class Combo (
    val cards: Array<String>,
    val colorIdentity: String,
    val prerequisites: String,
    val steps: String,
    val results: String
        )