package magicchief.main.brewersservant.dataclass

import java.net.URI
import java.util.*

class RelatedCard (
    val id: UUID,
    val component: String,  // token, meld_part, meld_result, combo_piece
    val name: String
        )