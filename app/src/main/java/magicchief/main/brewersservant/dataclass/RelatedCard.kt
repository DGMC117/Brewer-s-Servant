package magicchief.main.brewersservant.dataclass

import java.net.URI
import java.util.*

class RelatedCard (
    var id: UUID? = null,
    var component: String? = null,  // token, meld_part, meld_result, combo_piece
    var name: String? = null
        )